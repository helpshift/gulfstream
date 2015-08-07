(ns gulfstream.core
  (:require [gulfstream.data.interop :as interop]
            [gulfstream.graph.css :as css]
            [gulfstream.graph.dom :as dom]
            [hara.common.string :as string])
  (:import [org.graphstream.graph.implementations MultiGraph]
           [org.graphstream.ui.view Viewer Viewer$ThreadingModel]
           [org.graphstream.ui.swingViewer GraphRenderer]))

(defonce ^:dynamic *current-viewer* nil)
(defonce ^:dynamic *current-graph* nil)
(defonce ^:dynamic *current-camera* nil)

(defn disable-mouse [viewer]
  (let [view     (.getDefaultView viewer)
        listener (-> view (.getMouseListeners) seq first)]
    (.removeMouseListener view listener)))

(defn add-viewer-listener [viewer {:keys [on-push on-release on-exit]}]
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

(defn stylesheet
  ([graph]
   (if-let [css (.getAttribute graph "ui.stylesheet")]
     (css/parse css)))
  ([graph styles]
   (if styles
     (.setAttribute graph "ui.stylesheet"
                    (interop/attribute-array (css/emit (seq styles)))))
   graph))

(defn attributes
  ([graph]
   (interop/get-attributes graph))
  ([graph attrs]
   (interop/set-attributes graph attrs)))

(defn dom
  ([graph]
   (dom/get-dom graph))
  ([graph dom]
   (dom/set-dom graph dom)))


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

(defn view
  ([graph]
   (let [viewer (Viewer. graph Viewer$ThreadingModel/GRAPH_IN_ANOTHER_THREAD)
         renderer (Viewer/newGraphRenderer)
         panel      (.addView viewer Viewer/DEFAULT_VIEW_ID renderer)]
    panel)))

 (defn graph
   ([] (graph {}))
   ([{:keys [style dom attributes name] :as config}]
    (let [graph  (MultiGraph. name)]
      (alter-var-root #'*current-graph* (constantly graph))
      (-> graph
          (set/dom dom)
          (graph/stylesheet style)
          (interop/set-attributes attributes))
      graph)))

(defn screenshot
  ([path] (screenshot *current-viewer* *current-graph* path))
  ([graph path] (screenshot *current-viewer* graph path))
  ([viewer graph path]
   (cond (nil? viewer)
         (throw (Exception. "Cannot save image without viewer"))
   
         (nil? graph)
         (throw (Exception. "Cannot save image without graph"))

         :else (.addAttribute graph "ui.screenshot" (interop/attribute-array path)))))

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
