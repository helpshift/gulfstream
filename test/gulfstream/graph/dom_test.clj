(ns gulfstream.graph.dom-test
  (:use midje.sweet)
  (:require [gulfstream.graph.dom :refer :all]
            [gulfstream.graph :as graph]
            [gulfstream.core :as gs]
            [hara.object :as object]))

(def NODES {:a {:label "a"}
            :b {:label "b"}
            :c {:label "c"}})

(defn reset-graph []
  (gs/graph {:dom NODES}))

^{:refer gulfstream.graph.dom/get-dom :added "0.1"}
(fact "Sets the nodes"
  (reset-graph)
  
  (get-dom gs/*current-graph*)
  => {:a {:label "a"}, :b {:label "b"}, :c {:label "c"}})

^{:refer gulfstream.graph.dom/element :added "0.1"}
(fact "Accesses the element in the graph, be it a node or a cess"
  (-> (element gs/*current-graph* :a)
      (object/to-data))
  => {:attributes {:label "a"}, :id :a})


^{:refer gulfstream.graph.dom/diff :added "0.1"}
(fact "Function to determine what to change if they are different from each other"
  (reset-graph)
  
  (diff gs/*current-graph* {:a {:label "a"}
                            :c {:label "c" :ui.class "hello"}
                            [:a :c] {}})
  => {:nodes {:- #{:b}},
      :attributes {:+ {:c {:ui.class "hello"},
                       [:a :c] {}}},
      :edges {:+ #{[:a :c]}}})

^{:refer gulfstream.graph.dom/patch :added "0.1"}
(fact "Takes in a graph and a diff of the graph and applies it"
  (reset-graph)
  
  (patch gs/*current-graph*
         {:nodes {:- #{:b}},
          :attributes {:+ {:c {:ui.class "hello"},
                           [:a :c] {}}},
          :edges {:+ #{[:a :c]}}})
  
  (graph/dom gs/*current-graph*)
  => {[:a :c] nil, :a {:label "a"}, :c {:label "c", :ui.class "hello"}})

^{:refer gulfstream.graph.dom/set-dom :added "0.1"}
(fact "Sets all elements in the dom"
  (reset-graph)

  (set-dom gs/*current-graph*
           {:a {:label "a"}
            :c {:label "c" :ui.class "hello"}
            [:a :c] {}})

  (graph/dom gs/*current-graph*)
  => {[:a :c] nil, :a {:label "a"}, :c {:label "c", :ui.class "hello"}})
