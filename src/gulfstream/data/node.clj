(ns gulfstream.data.node
  (:require [hara.object :as object])
  (import org.graphstream.graph.Node))

(comment
  (macroexpand-1 '(object/extend-stringlike
                   Node
                   {:tag "node"}))

  (macroexpand-1 '(object/extend-stringlike
                   Node
                   {:tag "node"}))
  (macroexpand-1 '(hara.object.string-like/extend-stringlike-class Node {:tag "node"}))

  (hara.object.string-like/extend-stringlike-class Node {:tag "node"})


  (object/extend-stringlike
   Node {:tag "node"})






  )
