(ns gulfstream.axis-test
  (:require [gulfstream.core :as gs]
            [gulfstream.graph.ui :as ui])
  (:import [org.graphstream.algorithm.generator
            RandomGenerator
            DorogovtsevMendesGenerator
            BarabasiAlbertGenerator]))

(comment
  

  (ui/stylesheet gs/*current-graph*)
  (ui/attributes gs/*current-graph*)

  (.getSourceNode (first (.getEdgeSet gs/*current-graph*)))
  
  (doto (BarabasiAlbertGenerator. 3))
  (-> (gs/graph {})
      (gs/display {}))

  (generator )
  
  

  (.* (RandomGenerator.) :name)

  (def rgen (DorogovtsevMendesGenerator.))
  (.addSink rgen gs/*current-graph*)

  (.begin rgen)
  (.nextEvents rgen))

(comment

  gs/*current-camera*
  #camera{:metrics {:diagonal 1.7320508075688772, :high-point {:x 2.0, :z 1.0, :y 1.0}, :low-point {:x 0.0, :z 0.0, :y 0.0}, :size {:x 1.0, :z 1.0, :y 1.0}}, :view-percent 1.0, :padding-ygu 0.0, :padding-ypx 30.0, :view-rotation 0.0, :graph-dimension 1.7320508075688772, :view-center {:x 0.5, :z 0.0, :y 0.5}, :padding-xgu 0.0, :padding-xpx 30.0}
  (.* gs/*current-camera* :name)
  ("Tx" "allNodesOrSpritesIn" "autoFit" "autoFitView" "center" "checkVisibility" "clone" "edgeContains" "equals" "finalize" "findNodeOrSpriteAt" "getClass" "getGraphDimension" "getGraphViewport" "getMetrics" "getPaddingXgu" "getPaddingXpx" "getPaddingYgu" "getPaddingYpx" "getSpritePosition" "getSpritePositionEdge" "getSpritePositionFree" "getSpritePositionNode" "getViewCenter" "getViewPercent" "getViewRotation" "graph" "gviewport" "gviewportDiagonal" "hashCode" "isEdgeVisible" "isNodeIn" "isSpriteIn" "isSpriteVisible" "isVisible" "metrics" "nodeContains" "nodeInvisible" "notify" "notifyAll" "oldTx" "padding" "popView" "pushView" "removeGraphViewport" "resetView" "rotation" "setAutoFitView" "setBounds" "setGraphViewport" "setPadding" "setViewCenter" "setViewPercent" "setViewRotation" "setViewport" "setZoom" "spriteContains" "spritePositionPx" "toString" "transformGuToPx" "transformPxToGu" "userView" "wait" "xT" "zoom")

  (.getViewRotation gs/*current-camera*)
  0.0
  (.getViewport gs/*current-camera*)
  (.setViewPercent gs/*current-camera* 5.0)
  (.getGraphViewport gs/*current-camera*)
  nil
  (.setViewRotation gs/*current-camera* (+ (.getViewRotation gs/*current-camera*) 10))
  (.setViewCenter gs/*current-camera* 0.1 0.5 0)
  (.setViewport gs/*current-camera*  1.0 1.0 0 1.0)

  (.setBounds gs/*current-camera* -1.0 1.0 -1.0 1.0 -1.0 1.0)
  (.* gs/*current-camera* "setViewport")
  
  (ui/attributes gs/*current-graph*)

  (-> (gs/graph {:links   {:o #{:x :y :z}
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
                                     
                                     [:node.axis  {:size "0"
                                                   :fill-mode "plain"
                                                   :stroke-mode "none"}]
                                     
                                     ]})

      
            
            (gs/display {:disable-auto-layout true
                         :disable-mouse true})))

