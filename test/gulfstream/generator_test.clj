(ns gulfstream.generator-test
  (:require [gulfstream.core :as gs]
            [gulfstream.generator :as gen]
            [gulfstream.dom :as dom]
            [hara.object :as object]))

(comment


  (gs/graph {:ui {:title "DOM"}
                 :links {:a #{}
                         :b #{:a :c}
                         :c #{}}
             :properties  {:ui.class {"axis" #{:a :c}}}})


  (dom/dom gs/*current-graph*)
  (dom/diff gs/*current-graph* {:a {:attributes {:ui.class2 "axis"}}
                                :d {:attributes {:ui.class "axis2"}}})
  
  {:attributes {:- {:a {:ui.class "axis"}},
                :+ {:d {:ui.class "axis2"},
                    :a {:ui.class2 "axis"}}}}

  (dom/renew gs/*current-graph*
             {:a {:attributes {:ui.class2 "axis"}}
              :d {:attributes {:ui.class "axis2"}}})
  
  {:edges {:- #{[:b :a] [:b :c]}},
   :nodes {:- #{:c :b}, :+ #{:d}},
   :attributes {:- {:a {:ui.class "axis"}},
                :+ {:d {:ui.class "axis2"},
                    :a {:ui.class2 "axis"}}}}
  

  (.* gs/*current-graph* #"removeAttr" :#)
  
  
  (dom/diff gs/*current-graph* {})
  
  
  
  
  
  (-> (gs/graph {:ui {:title "Barabasi"}
           
                 :generator {:type :barabasi-albert
                             :args []
                             :steps 100
                             :interval 100}})
      (gs/display))

  (object/to-data (object/access gs/*current-viewer* :default-view))
  => 
  {:x 0, :y 0, :camera {:metrics {:diagonal 25.490202843429277, :high-point {:x 6.801572674995721, :z 1.0, :y 5.782128610846108, :zero? false}, :low-point {:x -10.946716820078105, :z -1.0, :y -12.404368055588085, :zero? false}, :size {:x 17.748289495073827, :z 2.0, :y 18.186496666434195, :zero? false}}, :view-percent 1.0, :padding-ygu 0.0, :padding-ypx 30.0, :view-rotation 0.0, :graph-dimension 25.490202843429277, :view-center {:x -2.072572072541192, :z 0.0, :y -3.3111197223709876, :zero? false}, :padding-xgu 0.0, :padding-xpx 30.0}}

  (object/to-data (object/access gs/*current-viewer* [:default-view :camera :metrics]))
  

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



