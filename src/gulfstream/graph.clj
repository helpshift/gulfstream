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
  "creates a di-graph for visualization
   (-> (graph {:title \"Hello World\"
               :dom {:nodes {:a {:label \"A\"
                                 :ui.class \"top\" }
                             :b {:label \"B\"}
                             :c {:label \"C\"}}
                     :edges {[:a :b] {:label \"a->b\"}}}})
       (object/to-data))
  => {:title \"Hello World\",
       :dom {:nodes {:a {:label \"A\", :ui.class \"top\"},
                     :b {:label \"B\"},
                     :c {:label \"C\"}},
             :edges {[:a :b] {:label \"a->b\"}}},
       :attributes {}, :step 0.0, :index 0, :strict? true,
       :edge-set [{:attributes {:label \"a->b\"}, :id [:a :b]}],
       :node-set [{:attributes {:label \"C\"}, :id :c}
                  {:attributes {:label \"B\"}, :id :b}
                  {:attributes {:label \"A\", :ui.class \"top\"}, :id :a}]}"
  {:added "0.1"}
  ([] (graph {}))
  ([{:keys [style dom attributes title] :as config}]
   (let [graph  (MultiGraph. title)]
     (alter-var-root #'+current-graph+ (constantly graph))
     (->> (select-keys config [:dom :style :attributes :title])
          (object/access graph)))))

(defn element
  "accesses the element within a graph
   
   (-> (element +current-graph+ :a)
       object/to-data)
   => {:attributes {:label \"A\", :ui.class \"top\"}, :id :a}"
  {:added "0.1"}
  [graph id]
  (if (vector? id)
    (.getEdge graph (->> (map string/to-string id)
                         (join "->")))
    (.getNode graph (string/to-string id))))

(defn disable-mouse [viewer]
  (let [view     (.getDefaultView viewer)
        listener (-> view (.getMouseListeners) seq first)]
    (.removeMouseListener view listener)))

(defn display
  "displays the graph in a seperate window"
  {:added "0.1"}
  [graph]
  (let [attrs  (object/access graph :attributes)
        viewer (doto (.display graph)
                 (.setCloseFramePolicy org.graphstream.ui.view.Viewer$CloseFramePolicy/CLOSE_VIEWER))
        _      (if (:gs.disable-auto-layout attrs)
                 (.disableAutoLayout viewer))
        _      (if (:gs.disable-mouse attrs)
                 (disable-mouse viewer))]
    (alter-var-root #'+current-viewer+ (constantly viewer))
    (alter-var-root #'+current-camera+ (constantly (.getCamera (.getDefaultView viewer))))
    viewer))

(defn add-node-listener
  "adds a listener for updates to node click events"
  {:added "0.1"}
  [viewer {:keys [on-push on-release on-exit]}]
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

(defn add-key-listener
  "adds a listener for key change events"
  {:added "0.1"}
  [viewer {:keys [on-push on-release on-typed]}]
  (.addKeyListener (.getDefaultView viewer)
                   (reify java.awt.event.KeyListener
                     (keyPressed [_ e]
                       ((or on-push identity) e))
                     (keyReleased [_ e]
                       ((or on-release identity) e))
                     (keyTyped [_ e]
                       ((or on-typed identity) e))))
  viewer)
