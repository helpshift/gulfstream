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
