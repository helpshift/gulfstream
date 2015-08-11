(ns gulfstream.graph-test
  (:use midje.sweet)
  (:require [gulfstream.graph :refer :all]
            [hara.object :as object]))

^{:refer gulfstream.graph/graph :added "0.1"}
(fact "creates a di-graph for visualization"
  (-> (graph {:title "Hello World"
                          :dom {:a {:label "A"
                                    :ui.class "top"}
                                :b {:label "B"}
                                :c {:label "C"}
                                [:a :b] {:label "a->b"}}})
      (object/to-data))
  => {:title "Hello World"
      :dom {[:a :b] {:label "a->b"}
            :a {:label "A" :ui.class "top"}
            :b {:label "B"} :c {:label "C"}}
      :attributes {}
      :step 0.0
      :index 0
      :strict? true
      :edge-set [{:attributes {:label "a->b"} :id [:a :b]}]
      :node-set [{:attributes {:label "C"} :id :c}
                 {:attributes {:label "B"} :id :b}
                 {:attributes {:label "A" :ui.class "top"} :id :a}]})


^{:refer gulfstream.graph/element :added "0.1"}
(fact "accesses the element within a graph"
  
  (-> (element +current-graph+ :a)
      object/to-data)
  => {:attributes {:label "A", :ui.class "top"}, :id :a})

^{:refer gulfstream.graph/display :added "0.1"}
(fact "displays the graph in a seperate window")

^{:refer gulfstream.graph/add-node-listener :added "0.1"}
(fact "adds a listener for updates to node click events")

^{:refer gulfstream.graph/add-key-listener :added "0.1"}
(fact "adds a listener for key change events")
