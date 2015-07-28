(ns gulfstream.data.interop
  (:require [hara.object :as object]))

(defn element-attributes
  [element]
  (let [res (reduce (fn [out k]
                      (assoc out (keyword k) (.getAttribute element k)))
                    {}
                    (.getAttributeKeySet element))]
    (if-not (empty? res) res)))

(object/extend-maplike

 org.graphstream.graph.implementations.AbstractNode
 {:tag "node"
  :include []
  :getters {:id #(-> % .getId keyword)
            :attributes element-attributes}}

 org.graphstream.graph.implementations.AbstractEdge
 {:tag "edge"
  :include []
  :getters {:id #(vector (-> % .getSourceNode str keyword)
                         (-> % .getTargetNode str keyword))
            :attributes element-attributes}}

 org.graphstream.graph.implementations.AbstractGraph
 {:tag "graph"
  :exclude [:each-node :each-edge :node-iterator :edge-iterator
            :replay-controller :each-attribute-key
            :attribute-key-iterator]}

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


(comment
  )
