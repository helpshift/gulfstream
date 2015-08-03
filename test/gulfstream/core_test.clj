(ns gulfstream.core-test
  ;;(:use midje.sweet)
  (:require [gulfstream.core :refer :all]
            [gulfstream.graph :as graph]
            [gulfstream.graph.dom :as dom]
            [hara.object :as object]))

(comment
  (def g (graph))
  (display g)
  g
  

  (object/to-data g)
  {:attributes {}, :step 0.0, :index 0, :strict? true, :edge-set [{:id [:a :c]} {:id [:b :a]} {:attributes {:label "e->d"}, :id [:e :d]}], :node-set [{:id :c} {:id :b} {:id :a} {:attributes {:label "e"}, :id :e} {:attributes {:label "d"}, :id :d}]}
  
  (graph/stylesheet *current-graph* [[:node.axis {:fill-color "#00c"
                                                  :size "20px"}]
                                     [:node {:fill-color "green"}]
                                     [:node:clicked
                                      {:fill-color "red"}]
                                     [:edge {:arrow-shape "arrow"
                                             :arrow-size "10px, 10px"}]])
  (graph/dom *current-graph*)
  
  (graph/dom *current-graph* {[:b :a] {}
                              [:a :c] {}
                              [:e :d] {:label "e->d"}
                              :c {:ui.class "axis" :label "c"}
                              :b {:ui.class "axis" :label "b"}
                              :a {:ui.class "axis" :label "a"}
                              :d {:label "d"}
                              :e {:label "e"}})

  
  (screenshot *current-graph* "hello.png")
  
  (graph/expand {:links   {:o #{:x :y :z}
                           :x #{}
                           :y #{}
                           :z #{}
                           :a #{}}
                 :attributes  {:ui.class {"axis" #{:x :y :z :o}}}
                 :elements    {:a   {:label    "a"
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
                               [:o :z] {:label    "z-axis"}}})
  
  
  => {:y {:ui.class "axis", :xyz [0 1 0], :label "(0,1,0)"},
   [:o :z] {:label "z-axis"},
   :o {:ui.class "axis", :xyz [0 0 0] :label "(0,0,0)"},
   [:o :y] {:label "y-axis"},
   [:o :x] {:label "x-axis"},
   :z {:ui.class "axis", :xyz [0 0 1], :label "(0,0,1)"},
   :x {:ui.class "axis", :xyz [1 0 0], :label "(1,0,0)"},
   :a {:xyz [0.5 0.5 0.5], :label "a"}})
