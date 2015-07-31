(ns gulfstream.core
  (:require [gulfstream.data.interop :as interop]
            [gulfstream.graph :as graph]
            [gulfstream.graph.dom :as dom]
            [hara.common.string :as string])
  (:import [org.graphstream.graph.implementations MultiGraph]))

(def ^:dynamic *current-viewer* nil)
(def ^:dynamic *current-graph* nil)
(def ^:dynamic *current-camera* nil)

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
     (alter-var-root #'*current-viewer* (constantly viewer))
     (alter-var-root #'*current-camera* (constantly (.getCamera (.getDefaultView viewer))))
     viewer)))

 (defn graph
   ([] (graph {}))
   ([{:keys [style dom attributes name] :as config}]
    (let [graph  (MultiGraph. name)]
      (alter-var-root #'*current-graph* (constantly graph))
      (-> graph
          (dom/renew dom)
          (graph/stylesheet style)
          (interop/set-attributes attributes))
      graph)))
