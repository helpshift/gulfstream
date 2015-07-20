(ns gulfstream.core
  (import org.graphstream.graph.implementations.SingleGraph
          org.graphstream.graph.implementations.MultiGraph
          org.graphstream.ui.view.Viewer))


(defn display [graph]
  (doto (.display graph)
    (.setCloseFramePolicy org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER)))

(comment
  #_(System/setProperty "gs.ui.renderer" "org.graphstream.ui.j2dviewer.J2DGraphRenderer")

  (def graph (SingleGraph. "I Can See Nothing"))
  (iterator-seq (.getAttributeKeyIterator graph))
  (def viewer (display graph))
  (def viewer2 (.display graph))


  (.addAttribute graph "ui.stylesheet" (object-array ["node#A {
    shape: box;
    size: 15px, 20px;
    fill-mode: plain;   /* Default.          */
    fill-color: red;    /* Default is black. */
    stroke-mode: plain; /* Default is none.  */
    stroke-color: blue; /* Default is black. */
}"]))

  (.addAttribute graph "ui.stylesheet" (object-array ["graph {fill-color: red;}"]))
  org.graphstream.ui.view.Viewer$CloseFramePolicy/HIDE_ONLY
  (type (.? viewer :name #"^set"))

  (.* viewer :name)
  
  (.setCloseFramePolicy viewer2 org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER)
  (.setCloseFramePolicy viewer org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER)


  (.addNode graph "A")
  (.addNode graph "B")
  (.addNode graph "C")
  (.addEdge graph "AB", "A", "B")
  (.addEdge graph "BC", "B", "C")
  (.addEdge graph "CA", "C", "A")

  (.%> (.getNodeSet graph))
  [org.graphstream.graph.implementations.AbstractGraph$3 [java.util.AbstractCollection #{}] [java.lang.Object #{java.util.Collection}]]

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


  (.%> org.graphstream.graph.implementations.AbstractNode$4)
  [org.graphstream.graph.implementations.AbstractNode$4 [java.util.AbstractCollection #{}] [java.lang.Object #{java.util.Collection}]]

  )
