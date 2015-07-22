(ns gulfstream.data.interop
  (:require [hara.object :as object]))

(object/extend-stringlike

 org.graphstream.graph.implementations.AbstractNode
 {:tag "node"})

(object/extend-maplike

 org.graphstream.graph.implementations.AbstractGraph
 {:tag "graph"
  :exclude [:each-node :each-edge :node-iterator :edge-iterator
            :replay-controller :each-attribute-key
            :attribute-key-iterator]}

 org.graphstream.graph.implementations.AbstractEdge
 {:tag "edge"
  :select [:source-node :target-node :index :id]}

 org.graphstream.ui.view.Viewer
 {:tag "ui.viewer"
  :exclude [:graphic-graph]}

 org.graphstream.ui.view.View
 {:tag "ui.view"
  :select [:x :y :camera]}

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
  (object/extend-maplike AbstractNode
                         {:tag "node"
                          :select [:id :degree :attribute-key-set]}))
