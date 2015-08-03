(ns gulfstream.core
  (:require [gulfstream.data.interop :as interop]
            [gulfstream.graph :as graph]
            [gulfstream.graph.dom :as dom]
            [hara.common.string :as string])
  (:import [org.graphstream.graph.implementations MultiGraph]))

(defonce ^:dynamic *current-viewer* nil)
(defonce ^:dynamic *current-graph* nil)
(defonce ^:dynamic *current-camera* nil)

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
          (graph/dom dom)
          (graph/stylesheet style)
          (interop/set-attributes attributes))
      graph)))

(defn screenshot [viewer graph path]
  (.addAttribute graph "ui.screenshot" (interop/attribute-array path)))

(defn expand [{:keys [links attributes elements options] :as shortform}]
  (let [elements (reduce-kv (fn [elements source targets]
                              (reduce (fn [elements target]
                                        (update-in elements [[source target]] (fnil identity {})))
                                      (update-in elements [source] (fnil identity {}))
                                      targets))
                            elements
                            links)]
    (reduce-kv (fn [elements prop vmap]
                 (reduce-kv (fn [elements value tags]
                              (reduce (fn [elements tag]
                                        (assoc-in elements [tag prop] value))
                                      elements tags))
                            elements
                            vmap))
               elements
               attributes)))
