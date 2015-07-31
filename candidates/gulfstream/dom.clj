(ns gulfstream.dom
  (:require [gulfstream.data.interop]
            [hara.object :as object]
            [hara.common.string :refer [to-string]]
            [hara.data.diff :as diff]))

(defn dom [graph]
  (let [data (concat (object/to-data (.getNodeSet graph))
                     (object/to-data (.getEdgeSet graph)))]
    (zipmap (map :id data) (map #(dissoc % :id) data))))

(defn graph-element [graph key]
  (if (vector? key)
    (.getEdge graph (str (to-string (first key)) "->" (to-string (second key))))
    (.getNode graph (to-string key))))

(defn attribute-array [v]
  (cond (vector? v)
        (object-array v)
        
        :else
        (object-array [v])))

(defn patch [graph diff]
  (doseq [[source target] (-> diff :edges :-)]
    (.removeEdge graph (str (to-string source) "->" (to-string target))))
  (doseq [k (-> diff :nodes :-)]
    (.removeNode graph (to-string k)))
  (doseq [k (-> diff :nodes :+)]
    (.addNode graph (to-string k)))
  (doseq [[source target] (-> diff :edges :+)]
    (let [source (to-string source) 
          target (to-string target) ]
      (.addEdge graph (str source "->" target) source target true)))
  (reduce-kv (fn [graph key attrs]
               (let [ele (graph-element graph key)]
                 (doseq [k (keys attrs)]
                   (.removeAttribute ele (to-string k)))
                 graph))
             graph
             (-> diff :attributes :-))
  (reduce-kv (fn [graph key attrs]
               (let [ele (graph-element graph key)]
                 (reduce-kv (fn [ele ak av] 
                              (doto ele (.setAttribute (to-string ak) (attribute-array av))))
                            ele
                            attrs)
                 graph))
             graph
             (-> diff :attributes :+)))

(defn diff-element [out key sign]
  (let [type (if (vector? key) :edges :nodes)]
    (update-in out [type sign] (fnil #(conj % key) #{}))))

(defn diff-attributes [out arr v sign]
  (let [arr (concat [:attributes sign] arr)]
    (case (count arr)
      3 (update-in out arr merge (:attributes v))
      4 (update-in out (butlast arr) merge v)
      5 (assoc-in out (concat(take 3 arr) (list (last arr))) v))))

(defn diff [graph new]
  (let [changes (diff/diff new (dom graph))
        ele-fn  (fn [output pos-sign diff-sign]
                  (reduce-kv (fn [out arr v]
                               (if (= 1 (count arr))
                                 (if (= :+ diff-sign)
                                   (-> out
                                       (diff-element (first arr) pos-sign)
                                       (diff-attributes arr v pos-sign))
                                   (diff-element out (first arr) pos-sign))
                                 (diff-attributes out arr v pos-sign)))
                             output
                             (diff-sign changes)))]
    (-> {}
        (ele-fn :+ :+)
        (ele-fn :+ :>)
        (ele-fn :- :-))))

(defn renew [graph new]
  (->> (diff graph new)
       (patch graph)))


(comment
  (diff/diff
   {[:b :a] {:attributes {:ui.class "link"}}, [:b :c] {}, :c {:attributes {:ui.class "axis"}, :degree 1}, :b {:degree 2}, :a {:attributes {:ui.class "axis"}, :degree 1}}
   {[:b :a] {}, [:b :c] {}, :c {:attributes {:ui.class "axis"}, :degree 1}, :b {:degree 2}, :a {:attributes {:ui.class "axis"}, :degree 1}})

  (diff/diff
   {[:b :a] {}}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {},
      :- {[[:b :a] :attributes] {:ui.class "link"}},
      :+ {}}

  {:nodes {:+ [:b :c] :- []}
   :edges {:+ [[:b :c]] :- [[:c :a]]}
   :attributes {:+ {[:b :c] {:ui.class "axis"}}
                :- {[:b :c] {:ui.flip  true}}}}
  
  (diff/diff
   {}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {}, :- {[[:b :a]] {:attributes {:ui.class "link"}}}, :+ {}}

  (diff/diff
   {[:b :a] {:attributes {:ui.class "hello"}}}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {[[:b :a] :attributes :ui.class] "hello"},
      :- {},
      :+ {}})
