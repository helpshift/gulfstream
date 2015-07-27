(ns gulfstream.core
  (:require [gulfstream.data.interop :as interop]
            [gulfstream.graph.ui :as ui]
            [hara.common.string :as string])
  (:import [org.graphstream.graph.implementations SingleGraph MultiGraph]))

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

(defn create-links [graph links]
  (let [nodes (keys links)]
    (doseq [node (map string/to-string nodes)]
      (.addNode graph node))
    (doseq [node   (map string/to-string nodes)
            target (->> (get links (keyword node))
                        (map string/to-string))]
      (try
        (.addEdge graph (str node "->" target) node target true)
        (catch Throwable e
          (println "REJECTED:" (str node "->" target)))))
    graph))

(defn graph [{:keys [links attributes properties title style] :as content}]
  (let [graph  (MultiGraph. title)]
    (alter-var-root #'*current-graph* (constantly graph))
    (-> graph
        (create-links links)
        (ui/stylesheet style)
        (ui/attributes attributes)
        (ui/properties properties))
    graph))
