(ns gulfstream.core
  (:require [gulfstream.graph :as graph]
            [gulfstream.graph.dom :as dom]
            [gulfstream.util :as util]
            [hara.data.diff :as diff]
            [hara.object :as object]))

(defonce +current+ nil)

(defrecord Browser []
  clojure.lang.IFn
  (invoke [obj]     (object/access obj))
  (invoke [obj k]   (object/access obj k))
  (invoke [obj k v] (object/access obj k v)))

(defmethod print-method Browser
  [v w]
  (.write w (str (into {} v))))

(defn browse
  "returns a browser object for viewing and updating a graph. The browser includes
   a shadow dom so that any changes reflected within the shadow dom will be reflected in
   the front end"
  {:added "0.1"}
  [{:keys [dom style attributes title options listeners] :as m}]
  (let [graph   (graph/graph {:dom dom :style style :attributes attributes :title title})
        more    (dissoc m :dom :style :attributes :title)
        shadow  (atom dom)
        {:keys [keyboard node]} listeners
        viewer  (graph/display graph options)
        viewer  (if node
                  (graph/add-node-listener viewer node)
                  viewer)
        viewer  (if keyboard
                  (graph/add-key-listener viewer keyboard)
                  viewer)
        browser (map->Browser (merge {:graph  graph
                                      :dom    shadow
                                      :viewer viewer}
                                     more))]

    (add-watch shadow :pipeline
               (fn [_ _ p n]
                 (dom/patch-dom graph (diff/diff n p))))
    (alter-var-root #'+current+ (constantly browser))
    browser))

(object/extend-maplike

 Browser
 {:tag "browser"
  :default false
  ;; This will replace all getters and setters except the `:dom` key when using hara.object 2.2.6
  :proxy   {:graph [:attributes :style :title]}
  :getters {:attributes #(object/access (:graph %) :attributes)
            :style      #(object/access (:graph %) :style)
            :dom        #(-> % :dom deref)
            :title      #(object/access (:graph %) :title)}
  :setters {:attributes (fn [b attrs]
                          (object/access (:graph b) :attributes attrs)
                          b)
            :style      (fn [b style]
                          (object/access (:graph b) :style style)
                          b)
            :dom        (fn [b dom]
                          (reset! (:dom b) dom)
                          b)
            :title      (fn [b title]
                          (object/access (:graph b) :title title)
                          b)}})
