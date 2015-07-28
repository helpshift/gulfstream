(ns gulfstream.generator-test
  (:require [gulfstream.core :as gs]
            [gulfstream.generator :as gen]
            [gulfstream.dom :as dom]))

(comment

  (-> (gs/graph {:ui {:title "DOM"}
                 :links {:a #{}
                         :b #{:a :c}
                         :c #{}}
                 :properties  {:ui.class {"axis" #{:a :c}}}})
      (dom/dom))
  
  
  (-> (gs/graph {:ui {:title "Barabasi"}
           
                 :generator {:type :barabasi-albert
                             :args []
                             :steps 100
                             :interval 100}})
      (gs/display))
  


  (-> (gs/graph {:ui {:title "DOM"}
                 :links {:a #{}
                         :b #{:a :c}
                         :c #{}}
                 :properties  {:ui.class {"axis" #{:a :c}}}})
      
      (gs/display))
  
  (.* (.getEdge gs/*current-graph* "b->a") :name)
  (.* (.getEdge gs/*current-graph* "b->a") :name)
  ("addAttribute" "addAttributes" "attributeChanged" "attributes" "attributesBeingRemoved" "changeAttribute" "clearAttributes" "clearAttributesWithNoEvent" "clone" "directed" "equals" "finalize" "getArray" "getAttribute" "getAttributeCount" "getAttributeKeyIterator" "getAttributeKeySet" "getClass" "getEachAttributeKey" "getFirstAttributeOf" "getHash" "getId" "getIndex" "getLabel" "getNode0" "getNode1" "getNumber" "getOpposite" "getSourceNode" "getTargetNode" "getVector" "graph" "hasArray" "hasAttribute" "hasHash" "hasLabel" "hasNumber" "hasVector" "hashCode" "id" "index" "isDirected" "isLoop" "notify" "notifyAll" "nullAttributesAreErrors" "removeAttribute" "setAttribute" "setIndex" "source" "target" "toString" "wait")

  (.getNode gs/*current-graph* "a")

  


  )



