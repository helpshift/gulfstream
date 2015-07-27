(ns gulfstream.graph.ui
  (:require [gulfstream.graph.css :as css]
            [hara.common.string :as string]
            [clojure.string :as str]))

(defn stylesheet
  ([graph]
   (css/parse (.getAttribute graph "ui.stylesheet")))
  ([graph styles]
   (.setAttribute graph "ui.stylesheet"
                  (object-array [(css/emit (seq styles))]))
   graph))


(defn element [graph k]
  (cond (keyword? k)
        (.getNode graph (string/to-string k))

        (vector? k)
        (.getEdge graph (->> (map string/to-string k)
                             (str/join "->")))))

(defn attribute-array [v]
  (cond (vector? v)
        (object-array v)
        
        :else
        (object-array [v])))

(defn attributes
  ([graph]
   (->> (concat (for [node (.getNodeSet graph)
                      k    (.getAttributeKeySet node)]
                  [[(-> node str keyword) (keyword k)] (.getAttribute node k)])
                (for [edge (.getEdgeSet graph)
                      k    (.getAttributeKeySet edge)]
                  [[[(-> (.getSourceNode edge) str keyword)
                     (-> (.getTargetNode edge) str keyword)]
                    (keyword k)]
                   (.getAttribute edge k)]))
        (reduce (fn [out [arr v]]
                  (assoc-in out arr (if (.isArray (type v))
                                      (vec (seq v))
                                      v)))
                {})))
  ([graph attributes]
   (reduce-kv (fn [graph elek attrs]
                (let [ele (element graph elek)]
                  (reduce-kv (fn [graph k v]
                               (.setAttribute ele
                                              (string/to-string k)
                                              (attribute-array v))
                               graph)
                             graph attrs)))
              graph
              attributes)))

(defn properties [graph properties]
  (reduce-kv (fn [graph propk vals]
               (reduce-kv (fn [graph v eles]
                            (doseq [ele eles]
                              (.setAttribute (element graph ele) (string/to-string propk)
                                             (attribute-array v))
                              graph))
                          graph
                          vals))
             graph
             properties))

(comment :properties  {:ui.class {"axis" #{:x :y :z :o}}}
         :attributes  {:o   {:label    "(0,0,0)"
                             :xyz       [0 0 0]}
                       :x   {:label    "(1,0,0)"
                             :xyz       [1 0 0]}
                       :y   {:label    "(0,1,0)"
                             :xyz       [0 1 0]}
                       :z   {:label    "(0,0,1)"
                             :xyz       [0 0 1]}
                       [:o :x] {:label    "x-axis"}
                       [:o :y] {:label    "y-axis"}})
