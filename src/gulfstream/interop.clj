(ns gulfstream.interop
  (:require [hara.object :as object]
            [hara.common.string :as string]
            [gulfstream.util :as util]
            [gulfstream.graph.dom :as dom]
            [gulfstream.graph.css :as css]))

(defn get-attributes
  "gets the attribute of a graph, node or element as a map"
  {:added "0.1"}
  [element]
  (let [res (reduce (fn [out k]
                      (assoc out (keyword k) (.getAttribute element k)))
                    {}
                    (.getAttributeKeySet element))]
    (if-not (empty? res)
      (dissoc res :ui.stylesheet :ui.title))))

(defn set-attributes
  "sets the attribute of a graph, node or element in the form of a map"
  {:added "0.1"}
  [element attrs]
  (reduce-kv (fn [element k v]
               (if (or (nil? v)
                       (and (vector? v)
                            (empty? v)))
                 (.removeAttribute element (string/to-string k))
                 (.setAttribute element (string/to-string k) (util/attribute-array v)))
               element)
             element
             (dissoc attrs :ui.stylesheet :ui.title)))

(object/extend-maplike

 org.graphstream.graph.implementations.AbstractNode
 {:tag "node"
  :default false
  :getters {:id #(-> % .getId keyword)
            :attributes get-attributes}
  :setters {:attributes set-attributes}}

 org.graphstream.graph.implementations.AbstractEdge
 {:tag "edge"
  :default false
  :getters {:id #(vector (-> % .getSourceNode str keyword)
                         (-> % .getTargetNode str keyword))
            :attributes get-attributes}
  :setters {:attributes set-attributes}}

 org.graphstream.graph.implementations.AbstractGraph
 {:tag "graph"
  :include [:node-set :edge-set :strict? :index :step]
  :getters {:attributes get-attributes
            :dom dom/get-dom
            :style css/get-stylesheet
            :title #(.getAttribute % "ui.title")}
  :setters {:attributes set-attributes
            :dom dom/set-dom
            :style css/set-stylesheet
            :title #(.setAttribute %1 "ui.title" (util/attribute-array %2))}
  :hide    [:node-set :edge-set :strict? :index :step]}

 org.graphstream.ui.view.Viewer
 {:tag "ui.viewer"
  :exclude [:graphic-graph]}

 org.graphstream.ui.view.View
 {:tag "ui.view"
  :include [:x :y :camera]}

 org.graphstream.ui.view.Camera
 {:tag "camera"}

 org.graphstream.ui.swingViewer.util.GraphMetrics
 {:tag "metrics"}

 org.graphstream.ui.geom.Point2
 {:tag "point"
  :getters {:x #(.x %)
            :y #(.y %)}}

 org.graphstream.ui.geom.Point3
 {:tag "point"
  :getters {:x #(.x %)
            :y #(.y %)
            :z #(.z %)}}

 org.graphstream.ui.geom.Vector2
 {:tag "vector"
  :getters {:x #(.x %)
            :y #(.y %)}}

 org.graphstream.ui.geom.Vector3
 {:tag "vector"
  :getters {:x #(.x %)
            :y #(.y %)
            :z #(.z %)}})
