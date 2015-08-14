(ns gulfstream.graph.dom-test
  (:use midje.sweet)
  (:require [gulfstream.graph.dom :refer :all]
            [gulfstream.graph :as graph]
            [hara.object :as object]
            [hara.data.diff :as diff]))

(def NODES {:a {:label "a"}
            :b {:label "b"}
            :c {:label "c"}})

(defn reset-graph []
  (graph/graph {:dom {:nodes NODES
                      :edges {[:a :b] {:label "a->b"}}}}))
  
^{:refer gulfstream.graph.dom/get-dom :added "0.1"}
(fact "Gets the nodes and edges of the graph"
  (get-dom (reset-graph))
  => {:nodes {:a {:label "a"}, :b {:label "b"}, :c {:label "c"}}
      :edges {[:a :b] {:label "a->b"}}})

^{:refer gulfstream.graph.dom/node :added "0.1"}
(fact "Accesses a node within the graph"
  (-> (node (reset-graph) :a)
      (object/to-data))
  => {:attributes {:label "a"}, :id :a})

^{:refer gulfstream.graph.dom/edge :added "0.1"}
(fact "Accesses an edge within the graph"  
  (-> (edge (reset-graph) [:a :b])
      (object/to-data))
  => {:attributes {:label "a->b"}, :id [:a :b]})

^{:refer gulfstream.graph.dom/element :added "0.1"}
(fact "Accesses the element in the graph, be it a node or a cess"
  (-> (element (reset-graph) :a)
      (object/to-data))
  => {:attributes {:label "a"}, :id :a})

^{:refer gulfstream.graph.dom/patch :added "0.1"}
(fact "Takes in a graph and a patch of the graph and applies "
  (-> (reset-graph)
      (patch {:nodes {:- #{:b}},
              :attributes {:+ {:c {:ui.class "hello"},
                               [:a :c] {:ui.class "world"}}},
              :edges {:+ #{[:a :c]}
                      :- #{[:a :b]}}})
      (get-dom))
  => {:nodes {:a {:label "a"},
              :c {:label "c", :ui.class "hello"}},
      :edges {[:a :c] {:ui.class "world"}}})

^{:refer gulfstream.graph.dom/make-patch-element :added "0.1"}
(fact "creates an entry for patching a element"
  (make-patch-element {} :nodes :a :+)
  => {:nodes {:+ #{:a}}})

^{:refer gulfstream.graph.dom/make-patch-attributes :added "0.1"}
(fact "creates an entry for patching element attributes"
  (make-patch-attributes {} [:nodes :a] {:label "hello"} :-)
  => {:attributes {:- {:nodes {:a {:label "hello"}}}}}

  (make-patch-attributes {} [:nodes] {:a {:label "hello"}} :-)
  => {:attributes {:- {:nodes {:a {:label "hello"}}}}})

^{:refer gulfstream.graph.dom/make-patch :added "0.1"}
(fact "creates a patch for updating the graph from a data diff"
  (make-patch {:> {},
                   :- {},
                   :+ {[:nodes :c] {:label "c"},
                       [:nodes :b] {:label "b"},
                       [:nodes :a] {:label "a"}}})
  => {:attributes {:+ {:b {:label "b"},
                       :c {:label "c"},
                       :a {:label "a"}}},
      :nodes {:+ #{:c :b :a}}})

^{:refer gulfstream.graph.dom/diff-dom :added "0.1"}
(fact "diffs the graph with a set of changes to be made"
  
  (-> (reset-graph)
      (diff-dom {:> {},
                 :- {[:nodes :c] {:label "c"}}})
      (get-dom))
  => {:nodes {:b {:label "b"},
              :a {:label "a"}},
      :edges {[:a :b] {:label "a->b"}}})

^{:refer gulfstream.graph.dom/set-dom :added "0.1"}
(fact "sets the graph to the "
  (-> (reset-graph)
      (set-dom {:nodes {:d {:label "d"}
                        :e {:label "c"}}
                :edges {[:d :e] {:label "d->e"}}})
      (get-dom))
  => {:nodes {:d {:label "d"},
              :e {:label "c"}},
      :edges {[:d :e] {:label "d->e"}}})
