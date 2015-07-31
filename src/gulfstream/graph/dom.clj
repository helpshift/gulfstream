(ns gulfstream.graph.dom
  (:require [gulfstream.data.interop :as interop]
            [hara.data.diff :as diff]
            [hara.object :as object]
            [hara.common.string :as s]
            [clojure.string :as string]))

(defn get-dom [graph]
  (let [data (concat (object/to-data (.getNodeSet graph))
                     (object/to-data (.getEdgeSet graph)))]
    (zipmap (map :id data) (map :attributes data))))

(defn element [graph k]
  (cond (keyword? k)
        (.getNode graph (s/to-string k))

        (vector? k)
        (.getEdge graph (->> (map s/to-string k)
                             (string/join "->")))))

(defn patch [graph diff]
  (doseq [[source target] (-> diff :edges :-)]
    (.removeEdge graph (str (s/to-string source) "->" (s/to-string target))))
  (doseq [k (-> diff :nodes :-)]
    (.removeNode graph (s/to-string k)))
  (doseq [k (-> diff :nodes :+)]
    (.addNode graph (s/to-string k)))
  (doseq [[source target] (-> diff :edges :+)]
    (let [source (s/to-string source)
          target (s/to-string target) ]
      (.addEdge graph (str source "->" target) source target true)))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (doseq [k (keys attrs)]
                   (.removeAttribute ele (s/to-string k)))
                 graph))
             graph
             (-> diff :-))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (reduce-kv (fn [ele ak av]
                              (doto ele (.setAttribute (s/to-string ak) (interop/attribute-array av))))
                            ele
                            attrs)
                 graph))
             graph
             (-> diff :+)))

(defn diff-element [out key sign]
  (let [type (if (vector? key) :edges :nodes)]
    (update-in out [type sign] (fnil #(conj % key) #{}))))

(defn diff-attributes [out arr v sign]
  (let [arr (concat [sign] arr)]
    (case (count arr)
      2 (update-in out arr merge v)
      3 (assoc-in out arr v))))

(defn diff [graph new]
  (let [changes (diff/diff new (get-dom graph))
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

(defn set-dom [graph dom]
  (->> (diff graph dom)
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
