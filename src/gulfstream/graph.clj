(ns gulfstream.graph
  (:require [gulfstream.graph.css :as css]
            [gulfstream.graph.dom :as dom]
            [gulfstream.data.interop :as interop]))

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
