(ns gulfstream.graph
  (:require [hara.object :as object]
            [gulfstream.interop :as interop])
  (:import  [org.graphstream.graph.implementations MultiGraph]))

(defonce +current-viewer+ nil)
(defonce +current-graph+ nil)
(defonce +current-camera+ nil)

(defn graph
  ([] (graph {}))
  ([{:keys [style dom attributes name] :as config}]
   (let [graph  (MultiGraph. name)]
     (alter-var-root #'+current-graph+ (constantly graph))
     (-> graph
         (object/access :dom dom)
         (object/access :style style)
         (object/access :attributes attributes))
     graph)))

(defn disable-mouse [viewer]
  (let [view     (.getDefaultView viewer)
        listener (-> view (.getMouseListeners) seq first)]
    (.removeMouseListener view listener)))

(defn display
  ([graph] (display graph {}))
  ([graph options]
   (let [viewer (doto (.display graph)
                  (.setCloseFramePolicy org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER))
         _      (if (:disable-auto-layout options)
                  (.disableAutoLayout viewer))
         _      (if (:disable-mouse options)
                  (disable-mouse viewer))]
     (alter-var-root #'+current-viewer+ (constantly viewer))
     (alter-var-root #'+current-camera+ (constantly (.getCamera (.getDefaultView viewer))))
     viewer)))

(defn add-node-listener [viewer {:keys [on-push on-release on-exit]}]
  (let [pipe (doto (.newViewerPipe viewer)
               (.addViewerListener (reify org.graphstream.ui.view.ViewerListener
                                     (buttonPushed [_ id]
                                       ((or on-push identity) id))
                                     (buttonReleased [_ id]
                                       ((or on-release identity) id))
                                     (viewClosed [_ name]
                                       ((or on-exit identity) name)))))]
    (future
      (loop []
        (if (and (.getDefaultView viewer)
                 (.isShowing (.getDefaultView viewer)))
          (do (.blockingPump pipe)
              (recur)))))
    viewer))

(defn add-key-listener [viewer {:keys [on-push on-release on-typed]}]
  (.addKeyListener (.getDefaultView viewer)
                   (reify java.awt.event.KeyListener
                     (keyPressed [_ e]
                       ((or on-push identity) e))
                     (keyReleased [_ e]
                       ((or on-release identity) e))
                     (keyTyped [_ e]
                       ((or on-typed identity) e))))
  viewer)


(comment
  (display (graph {:dom {:a {:label "a"}
                         :b {:label "b"}
                         [:a :b] {}}
                   :style [[:node.selected {:size "20px"}]]})))
