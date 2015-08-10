(ns gulfstream.graph
  (:require [hara.object :as object]
            [hara.common.string :as string]
            [clojure.string :refer [join]]
            [gulfstream.interop :as interop])
  (:import  [org.graphstream.graph.implementations MultiGraph]))

(defonce +current-viewer+ nil)
(defonce +current-graph+ nil)
(defonce +current-camera+ nil)

(defn graph
  ([] (graph {}))
  ([{:keys [style dom attributes title] :as config}]
   (let [graph  (MultiGraph. title)]
     (alter-var-root #'+current-graph+ (constantly graph))
     (->> (select-keys config [:dom :style :attributes :title])
          (object/access graph)))))

(defn element [graph id]
  (if (vector? id)
    (.getEdge graph (->> (map string/to-string id)
                         (join "->")))
    (.getNode graph (string/to-string id))))

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
                   :style [[:node.selected {:size "20px"}]]}))
  +current-graph+
  )
