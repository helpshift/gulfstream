(ns gulfstream.core
  (:import [org.graphstream.graph.implementations SingleGraph MultiGraph]
           org.graphstream.ui.view.Viewer))

(defn display [graph]
  (doto (.display graph)
    (.setCloseFramePolicy org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER)))

(defn style [graph css]
  (.addAttribute graph "ui.stylesheet"
                 (object-array css)))

(defn graph [{:keys [type title style items] :as content}]
  (let [grph  (SingleGraph. title)
        nodes (keys items)]
    (doseq [node nodes]
      (.addNode grph (str node)))
    (doseq [node nodes]
      (let [targets (get items node)]
        (doseq [target targets]
          (try
            (.addEdge grph (str (name target) "->" (name node)) (str node) (str target))
            (catch Throwable e
                (println "REJECTED:" (str node "->" target)))))))
    grph))

(comment
  #_(System/setProperty "gs.ui.renderer" "org.graphstream.ui.j2dviewer.J2DGraphRenderer")

  (def graph (SingleGraph. "I Can See Nothing"))
  (def viewer (display graph))

  (./pull '[rhizome "0.2.5"])

  

  (.addAttribute graph "ui.stylesheet" (object-array ["node#A {
    shape: box;
    size: 30px, 30px;
    fill-mode: plain;   /* Default.          */
    fill-color: green;    /* Default is black. */
    stroke-mode: plain; /* Default is none.  */
    stroke-color: blue; /* Default is black. */
}"]))

  (.attachToNode (.getNode graph "A") "A")
  (.* (.getNode graph "A") :name)
  ("addAttribute" "addAttributes" "addEdgeCallback" "attributeChanged" "attributes" "attributesBeingRemoved" "changeAttribute" "clearAttributes" "clearAttributesWithNoEvent" "clearCallback" "clone" "degree" "edgeType" "edges" "equals" "finalize" "getArray" "getAttribute" "getAttributeCount" "getAttributeKeyIterator" "getAttributeKeySet" "getBreadthFirstIterator" "getClass" "getDegree" "getDepthFirstIterator" "getEachAttributeKey" "getEachEdge" "getEachEnteringEdge" "getEachLeavingEdge" "getEdge" "getEdgeBetween" "getEdgeFrom" "getEdgeIterator" "getEdgeSet" "getEdgeToward" "getEnteringEdge" "getEnteringEdgeIterator" "getEnteringEdgeSet" "getFirstAttributeOf" "getGraph" "getHash" "getId" "getInDegree" "getIndex" "getLabel" "getLeavingEdge" "getLeavingEdgeIterator" "getLeavingEdgeSet" "getNeighborNodeIterator" "getNumber" "getOutDegree" "getVector" "graph" "hasArray" "hasAttribute" "hasEdgeBetween" "hasEdgeFrom" "hasEdgeToward" "hasHash" "hasLabel" "hasNumber" "hasVector" "hashCode" "id" "index" "ioStart" "isEnteringEdge" "isIncidentEdge" "isLeavingEdge" "iterator" "locateEdge" "neighborMap" "notify" "notifyAll" "nullAttributesAreErrors" "oStart" "removeAttribute" "removeEdge" "removeEdgeCallback" "setAttribute" "setIndex" "toString" "wait")
  
  
  (type (.? viewer :name #"^set"))

  (.* viewer :name)
  
  (hara.object/to-data (.getNodeSet graph))
  
  
  
  
  (.? java.util.Collection :name)
  ("add" "addAll" "clear" "contains" "containsAll" "equals" "hashCode" "isEmpty" "iterator" "remove" "removeAll" "retainAll" "size" "toArray")
  (.? java.util.AbstractCollection :name)
  ("MAX_ARRAY_SIZE" "add" "addAll" "clear" "contains" "containsAll" "finishToArray" "hugeCapacity" "isEmpty" "iterator" "new" "remove" "removeAll" "retainAll" "size" "toArray" "toString")

  
  (do (.addNode graph "A")
      (.addNode graph "B")
      (.addNode graph "C")
      (.addEdge graph "AB", "A", "B")
      (.addEdge graph "BC", "B", "C")
      (.addEdge graph "CA", "C", "A"))

  (iterator-seq (.iterator (.getNodeSet graph)))
  [org.graphstream.graph.implementations.AbstractGraph$3 [java.util.AbstractCollection #{}] [java.lang.Object #{java.util.Collection}]]

  ( (.getNode graph "A"))
  ("addAttribute" "addAttributes" "addEdgeCallback" "attributeChanged" "attributes" "attributesBeingRemoved" "changeAttribute" "clearAttributes" "clearAttributesWithNoEvent" "clearCallback" "clone" "degree" "edgeType" "edges" "equals" "finalize" "getArray" "getAttribute" "getAttributeCount" "getAttributeKeyIterator" "getAttributeKeySet" "getBreadthFirstIterator" "getClass" "getDegree" "getDepthFirstIterator" "getEachAttributeKey" "getEachEdge" "getEachEnteringEdge" "getEachLeavingEdge" "getEdge" "getEdgeBetween" "getEdgeFrom" "getEdgeIterator" "getEdgeSet" "getEdgeToward" "getEnteringEdge" "getEnteringEdgeIterator" "getEnteringEdgeSet" "getFirstAttributeOf" "getGraph" "getHash" "getId" "getInDegree" "getIndex" "getLabel" "getLeavingEdge" "getLeavingEdgeIterator" "getLeavingEdgeSet" "getNeighborNodeIterator" "getNumber" "getOutDegree" "getVector" "graph" "hasArray" "hasAttribute" "hasEdgeBetween" "hasEdgeFrom" "hasEdgeToward" "hasHash" "hasLabel" "hasNumber" "hasVector" "hashCode" "id" "index" "ioStart" "isEnteringEdge" "isIncidentEdge" "isLeavingEdge" "iterator" "locateEdge" "neighborMap" "notify" "notifyAll" "nullAttributesAreErrors" "oStart" "removeAttribute" "removeEdge" "removeEdgeCallback" "setAttribute" "setIndex" "toString" "wait")
  (type (.getNode graph "A"))
  (.%> org.graphstream.graph.implementations.SingleNode)
  [org.graphstream.graph.implementations.SingleNode
   [org.graphstream.graph.implementations.AdjacencyListNode #{}]
   [org.graphstream.graph.implementations.AbstractNode #{}]
   [org.graphstream.graph.implementations.AbstractElement #{org.graphstream.graph.Node}]
   [java.lang.Object #{org.graphstream.graph.Element}]]

  (.? org.graphstream.graph.Node :name)
                                        ;> ("getBreadthFirstIterator" "getDegree" "getDepthFirstIterator" "getEachEdge" "getEachEnteringEdge" "getEachLeavingEdge" "getEdge" "getEdgeBetween" "getEdgeFrom" "getEdgeIterator" "getEdgeSet" "getEdgeToward" "getEnteringEdge" "getEnteringEdgeIterator" "getEnteringEdgeSet" "getGraph" "getInDegree" "getLeavingEdge" "getLeavingEdgeIterator" "getLeavingEdgeSet" "getNeighborNodeIterator" "getOutDegree" "hasEdgeBetween" "hasEdgeFrom" "hasEdgeToward" "toString")
  
  (type (.getEdgeSet (.getNode graph "A")))
  (hara.object/-to-data (.getNode graph "A"))
  (hara.object/to-data (.getNode graph "A"))
  (hara.object/meta-object (type (.getNode graph "A")))
  (type (.getNode graph "A"))
  (.%> org.graphstream.graph.implementations.SingleNode)

  (instance? java.lang.Iterable (.getNode graph "A"))
  [org.graphstream.graph.implementations.SingleNode
   [org.graphstream.graph.implementations.AdjacencyListNode #{}] [org.graphstream.graph.implementations.AbstractNode #{}] [org.graphstream.graph.implementations.AbstractElement #{org.graphstream.graph.Node}] [java.lang.Object #{org.graphstream.graph.Element}]]
  
  ((:to-data (hara.object/meta-object (type (.getNode graph "A")))
              ) (.getNode graph "A"))

  (hara.object/to-data (.getNode graph "A"))
  

  
  (hara.protocol.string/-to-string (.getNode graph "A"))

  (.%> org.graphstream.graph.implementations.AbstractNode$4)
  [org.graphstream.graph.implementations.AbstractNode$4 [java.util.AbstractCollection #{}] [java.lang.Object #{java.util.Collection}]]

  )
