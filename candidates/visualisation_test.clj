(ns gulfstream.visualisation-test
  (:require [hara.object :as object]
            [hara.protocol.data :as data]
            [hara.protocol.map :as map]
            [hara.object.util :as util]
            [hara.object.access :as access]
            [hara.reflect :as reflect])
  (:import [org.graphstream.graph.implementations SingleGraph MultiGraph]
           org.graphstream.ui.view.Viewer
           org.graphstream.ui.spriteManager.SpriteManager
           org.graphstream.ui.graphicGraph.stylesheet.StyleConstants$Units
           org.graphstream.algorithm.Toolkit))

(System/setProperty "gs.ui.renderer" "org.graphstream.ui.j2dviewer.J2DGraphRenderer")

(comment
  (def mgrph (MultiGraph. "Bazinga"))

  (def viewer (.display mgrph))
  (.setCloseFramePolicy viewer org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER)
  
  
  (util/clojure->java (name :close-frame-policy))
  (reflect/apply-element viewer "getCloseFramePolicy" [])
  
  (reflect/apply-element viewer "getCloseFramePolicy" [])

  (.* viewer :name)
  (object/object-setters viewer)
  (access/access viewer)
  (access/access viewer :close-frame-policy)
  (access/access viewer :close-frame-policy "CLOSE_VIEWER")
  
  (access viewer :--list)
  (access viewer)
  
  
  (defn % [x] x)
  
  ;; Add some nodes
  (do (.addNode mgrph "A")
      (.addNode mgrph "B")
      (.addNode mgrph "C")
      (.addNode mgrph "D")
      (.addEdge mgrph "AB" "A" "B")
      (.addEdge mgrph "CB" "C" "B")
      (.addEdge mgrph "CA" "C" "A"))

  (.setAttribute (.getNode mgrph "A") "ui.class" (object-array ["foo"]))

  (.setAttribute (.getNode mgrph "A") "ui.class" (object-array ["foo"]))
  
  (.%> (.getNode mgrph "C"))
  (object/to-data )
  (map/-to-map (.getNode mgrph ""))
  (object/to-data mgrph)
  (object/to-data (.getEdge mgrph "CA"))
  
  (keys (:default-view (object/to-data viewer)))

  (util/object-data (:default-view (object/to-data viewer)) identity)

  (def view (:default-view viewer))
  
  (util/object-data (:camera (:default-view (object/to-data viewer))))
  
  
  
  
  (object/to-data viewer)
  
  

  
  (util/object-data
   (:camera (:default-view (object/to-data viewer))))
  
  

  
  (keys getters)
  ((:camera getters) view)
  ((:y getters) view)
  
  
  
  
  
  (dissoc (util/object-data (.getNode mgrph "C")) :graph)
  
  
  
  
  (hara.object.map-like/generic-map (.getEdge mgrph "CA")
                                    {:tag "edge", :select [:source-node :target-node :index :id]})

  (type (.getSourceNode (.getEdge mgrph "CA")))
  (object/to-data (.getSourceNode (.getEdge mgrph "CA")))
  (data/-to-data (.getSourceNode (.getEdge mgrph "CA")))
  
  (util/object-data (.getEdge mgrph "CA"))
  (+ 1 1)
   
  
   
  
  (.* (.getNode mgrph "D") :name)
  (.getAttributeKeySet (.getNode mgrph "D"))

  (.%> mgrph)
  [org.graphstream.graph.implementations.MultiGraph [org.graphstream.graph.implementations.AdjacencyListGraph #{}] [org.graphstream.graph.implementations.AbstractGraph #{}] [org.graphstream.graph.implementations.AbstractElement #{org.graphstream.stream.Replayable org.graphstream.graph.Graph}] [java.lang.Object #{org.graphstream.graph.Element}]]
  (def methods (object/to-data mgrph))

  (util/object-apply methods mgrph )
  
  (object/to-data (.getNode mgrph "D"))
  (object/to-data (.getNodeSet mgrph))

  (util/object-getters mgrph)

  (reflect/query-instance mgrph [#"(^get)|(^is)|(^has)[A-Z]" 1 :instance :name])

  (iterator-seq (.%> (.getEachNode mgrph)))
  (iterator-seq (.iterator (.getNodeSet mgrph)))
  (def output (util/object-data mgrph))
  (def output (util/object-getters mgrph))

  output
  
  
  (keys output)

  (.* mgrph :name 1 #"^get")
  (.* mgrph :name 1 #"^set")
  (.* mgrph :name 1 #"^is")
  (.* mgrph :name 1 #"^has")
  
  
  (.* mgrph :name 1)
  
  ;; Play around with layout
  (.disableAutoLayout viewer)
  (.enableAutoLayout viewer)
  
  (do (def nodeA (.getNode mgrph "A"))
      (def nodeB (.getNode mgrph "B"))
      (def nodeC (.getNode mgrph "C")))

  ;; Setting and Getting Attributes
  (.setAttribute nodeA "ui.class" (object-array ["foo"]))
  (.setAttribute nodeA "ui.class" (object-array ["foo" "bar"]))
  (.clearAttributes nodeC)
  (iterator-seq (.getAttributeKeyIterator nodeA))
  (seq (.getAttributeKeySet nodeA))
  (seq (.getAttribute nodeA "ui.class"))
  (= (.getNode mgrph "A")
     (.getNode mgrph "A")
     nodeA)
  (.setAttribute nodeA "label" (object-array ["A"]))
  (.getAttribute mgrph "ui.stylesheet")
  (.clearAttributes mgrph)
  (.setAttribute mgrph "ui.stylesheet" (object-array []))

  (.setAttribute mgrph "ui.stylesheet" (object-array ["
node:clicked{
fill-mode: gradient-radial;
fill-color: red, white;}
node.bar {
    text-mode: normal;
    shape: circle;
    text-color: white;
    size: 30px, 30px;
    fill-mode: gradient-radial;   /* Default.          */
    fill-color: blue, white;    /* Default is black. */
    stroke-mode: none; /* Default is none.  */
    stroke-color: blue; /* Default is black. */
}"]))

  (.setAttribute nodeA "x" (object-array [1]))
  (.setAttribute nodeA "x" (object-array [100]))
  (.setAttribute nodeA "xyz" (object-array [1 1 1]))
  (.setAttribute nodeA "xyz" (object-array [3 3 3]))

  (def sman (SpriteManager. mgrph))

  (def s (.addSprite sman "S1"))
  (.setPosition s 10 1 1)
  (.attachToNode s "A")
  (.attachToEdge s "AB")
  (.setPosition s 0.5)
  (.setPosition s StyleConstants$Units/PX 0 0 0)

  ;; Rendering Quality
  (.addAttribute mgrph "ui.quality" (object-array []))
  (.addAttribute mgrph "ui.antialias" (object-array []))

  ;; Viewer
  (def v (.getDefaultView viewer))
  (def cam (.getCamera v))

  (.setViewCenter cam 0 0 0)
  (.setViewPercent cam 4.5)
  (.resetView cam)


  ;; Mouse Events
  (.* viewer :name)
  ("actionPerformed" "addDefaultView" "addView" "clone" "close" "closeFramePolicy" "computeGraphMetrics" "delay" "disableAutoLayout" "enableAutoLayout" "enableXYZfeedback" "equals" "finalize" "getClass" "getCloseFramePolicy" "getDefaultView" "getGraphicGraph" "getView" "graph" "graphInAnotherThread" "hashCode" "init" "layoutPipeIn" "newGGId" "newThreadProxyOnGraphicGraph" "newViewerPipe" "notify" "notifyAll" "optLayout" "pumpPipe" "removeView" "replayGraph" "setCloseFramePolicy" "sourceInSameThread" "timer" "toString" "views" "wait")

  (def fromViewer (.newViewerPipe viewer))

   (.? fromViewer :name)
   
  (def thrd (future (loop [_ nil]
                      (Thread/sleep 100)
                      (recur (.pump fromViewer)))))
  (future-done? thrd)
  (future-cancel thrd)
  (+ 1 1)

  (.? fromViewer :name)
  
  (.addViewerListener fromViewer listener)

  ;; Toolkit
  (seq (Toolkit/nodePosition nodeA))
  (Toolkit/averageDegree mgrph)
  

(def listener (reify org.graphstream.ui.view.ViewerListener
                (viewClosed [this id]
                  (println "View Closed:" id))
                (buttonPushed [this id]
                  (println "Button Pushed:" id))
                (buttonReleased [this id]
                  (println "Button Released:" id))))

(defn function [form]
  (eval (cons 'fn form)))



(binding [*data-readers* (merge *data-readers*
                                {'fn #'function})]
  ((read-string "#fn ([x] x)") 1)))



