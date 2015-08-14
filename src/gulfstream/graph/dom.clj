(ns gulfstream.graph.dom
  (:require [gulfstream.util :as util]
            [hara.data.diff :as diff]
            [hara.object :as object]
            [hara.common.string :as s]
            [clojure.string :as string]))

(defn get-dom
  "Sets the nodes
   (reset-graph)
   
   (get-dom graph/+current-graph+)
   => {:nodes {:a {:label \"a\"}, :b {:label \"b\"}, :c {:label \"c\"}}}"
  {:added "0.1"}
  [graph]
  (let [to-map (fn [data] (zipmap (map :id data) (map (fn [data] (or (:attributes data) {})) data)))]
    (hash-map :edges (to-map (object/to-data (.getEdgeSet graph)))
              :nodes (to-map (object/to-data (.getNodeSet graph))))))

(defn node
  "Accesses a node in the graph"
  {:added "0.1"}
  [graph k]
  (.getNode graph (s/to-string k)))

(defn edge
  "Accesses an edge in the graph"
  {:added "0.1"}
  [graph k]
  (.getEdge graph (->> (map s/to-string k)
                       (string/join "->"))))

(defn element
  "Accesses the element in the graph, be it a node or a cess
   (-> (element graph/+current-graph+ :a)
       (object/to-data))
   => {:attributes {:label \"a\"}, :id :a}"
  {:added "0.1"}
  [graph k]
  (cond (or (string? k) (keyword? k) (symbol? k))
        (node graph k)

        (vector? k)
        (edge graph k)))

(defn make-patch-element [m type k sign]
  (update-in m [type sign] (fnil #(conj % k) #{})))

(defn make-patch-attributes [out arr v sign]
  (let [arr (concat [:attributes sign] arr)]
    (case (count arr)
      3 (update-in out arr merge v)
      4 (assoc-in out arr v))))

(defn make-patch
    {:added "0.1"}
    [diff]
    (let [ele-fn  (fn [output pos-sign diff-sign]
                    (reduce-kv (fn [out arr v]
                                 (if (= 2 (count arr))
                                   (let [out (make-patch-element out (first arr) (second arr) pos-sign)]
                                     (if (= :+ diff-sign)
                                       (make-patch-attributes out (rest arr) v pos-sign)
                                       out))
                                   (make-patch-attributes out (rest arr) v pos-sign)))
                               output
                               (diff-sign diff)))]
    (-> {}
        (ele-fn :+ :+)
        (ele-fn :+ :>)
        (ele-fn :- :-))))

(defn patch
  "Takes in a graph and a diff of the graph and applies it
   (reset-graph)
   
   (patch graph/+current-graph+
          {:nodes {:- #{:b}},
           :attributes {:+ {:c {:ui.class \"hello\"},
                            [:a :c] {}}},
           :edges {:+ #{[:a :c]}}})
   
   (get-dom graph/+current-graph+)
   => {[:a :c] nil, :a {:label \"a\"}, :c {:label \"c\", :ui.class \"hello\"}}"
  {:added "0.1"}
  [graph patch]
  (doseq [[source target] (-> patch :edges :-)]
    (.removeEdge graph (str (s/to-string source) "->" (s/to-string target))))
  (doseq [k (-> patch :nodes :-)]
    (.removeNode graph (s/to-string k)))
  (doseq [k (-> patch :nodes :+)]
    (.addNode graph (s/to-string k)))
  (doseq [[source target] (-> patch :edges :+)]
    (let [source (s/to-string source)
          target (s/to-string target) ]
      (.addEdge graph (str source "->" target) source target true)))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (doseq [k (keys attrs)]
                   (.removeAttribute ele (s/to-string k)))
                 graph))
             graph
             (-> patch :attributes :-))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (reduce-kv (fn [ele ak av]
                              (doto ele (.setAttribute (s/to-string ak) (util/attribute-array av))))
                            ele
                            attrs)
                 graph))
             graph
             (-> patch :attributes :+)))

(defn diff-dom [graph diff]
  (->> (make-patch diff)
       (patch graph)))

(defn set-dom
  {:added "0.1"}
  [graph dom]
  (->> (diff/diff (merge {:nodes {} :edges {}} dom) (get-dom graph))
       (diff-dom graph)))
