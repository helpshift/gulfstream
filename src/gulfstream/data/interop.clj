(ns gulfstream.data.interop
  (:require [hara.object :as object]
            [hara.common.string :refer [to-string]]
            [gulfstream.data.common :as common]))

(defn get-attributes
  [element]
  (let [res (reduce (fn [out k]
                      (assoc out (keyword k) (.getAttribute element k)))
                    {}
                    (.getAttributeKeySet element))]
    (if-not (empty? res)
      (dissoc res :ui.stylesheet))))

(defn set-attributes
  [element attrs]
  (reduce-kv (fn [element k v]
               (.setAttribute element (to-string k) (common/attribute-array v))
               element)
             element
             (dissoc attrs :ui.stylesheet)))

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
  :getters {:attributes get-attributes}
  :setters {:attributes set-attributes}}

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
