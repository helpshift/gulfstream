(ns gulfstream.axis-test
  (:require [gulfstream.core :as gs]
            [gulfstream.graph.ui :as ui])
  (:import [org.graphstream.algorithm.generator
            RandomGenerator
            DorogovtsevMendesGenerator]))

(comment
  

  (ui/stylesheet gs/*current-graph*)
  (ui/attributes gs/*current-graph*)

  (.getSourceNode (first (.getEdgeSet gs/*current-graph*)))
  
  (map #(.getAttributeKeySet %) (.getNodeSet gs/*current-graph*))
  (->> (for [node (.getNodeSet gs/*current-graph*)
                k    (.getAttributeKeySet node)]
            [[(-> node str keyword) (keyword k)] (.getAttribute node k)])
          (reduce (fn [out [arr v]]
                    (assoc-in out arr (cond (.isArray (type v))
                                            (vec (seq v))

                                            :else v)))
                  {}))

  (.*  (first (.getNodeSet gs/*current-graph*)) :name)
  
  org.graphstream.graph.implementations.MultiGraph

  (.* (RandomGenerator.) :name)

  (def rgen (DorogovtsevMendesGenerator.))
  (.addSink rgen gs/*current-graph*)

  (.begin rgen)
  (.nextEvents rgen))

(comment
  
  (ui/attributes gs/*current-graph*)
  
  (doto (-> (gs/graph {:links   {:o #{:x :y :z}
                                 :x #{}
                                 :y #{}
                                 :z #{}
                                 :a #{}}
                       :properties  {:ui.class {"axis" #{:x :y :z :o}}}
                       :attributes  {:a   {:label    "a"
                                           :xyz       [0.5 0.5 0.5]}
                                     :o   {:label    "(0,0,0)"
                                           :xyz       [0 0 0]}
                                     :x   {:label    "(1,0,0)"
                                           :xyz       [1 0 0]}
                                     :y   {:label    "(0,1,0)"
                                           :xyz       [0 1 0]}
                                     :z   {:label    "(0,0,1)"
                                           :xyz       [0 0 1]}
                                     [:o :x] {:label    "x-axis"}
                                     [:o :y] {:label    "y-axis"}
                                     [:o :z] {:label    "z-axis"}}
                       :options     {:label-nodes true
                                     :label-edges true}
                       :style       [[:edge {:arrow-shape "arrow"
                                             :arrow-size  "10, 10"}]
                                     ;;[:node {:shape "freeplane"}]
                                     [:node.axis  {:size "0"
                                                   :fill-mode "plain"
                                                   :stroke-mode "none"}]
                                     ;;[:graph {:fill-color "red"}]
                                     ]})
            
            (gs/display {:disable-auto-layout true
                         :disable-mouse true}))))

