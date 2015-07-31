(ns gulfstream.object-test
  (:require [hara.object.util :as util]
            [hara.object :as object]
            [hara.reflect :as reflect]
            [gulfstream.dom :as dom]
            [gulfstream.graph.ui :as ui]
            [gulfstream.core :as gs]))

(comment
  ;; Create a Graph
  (gs/graph {:ui {:title "DOM"}
             :links {:a #{}
                     :b #{:a :c}
                     :c #{}}
             :properties  {:ui.class {"axis" #{:a :c}}}})

  ;; Convert Graph into Data
  (object/to-data gs/*current-graph*)
  ;;=>

  ;; Show Window
  (gs/display gs/*current-graph*)

  ;; Convert Viewer into Data
  (object/to-data gs/*current-viewer*)

  ;; Access Elements in the Graph
  (dom/dom gs/*current-graph*)
  {[:b :a] {}, [:b :c] {}, :c {:attributes {:ui.class "axis"}}, :b {}, :a {:attributes {:ui.class "axis"}}}

  ;; Update Elements in the Graph
  (dom/renew
   gs/*current-graph*
   {[:b :a] {}
    [:a :c] {}
    [:e :d] {:attributes {:label "e->d"}}
    :c {:attributes {:ui.class "axis" :label "c"}}
    :b {:attributes {:ui.class "axis" :label "b"}}
    :a {:attributes {:ui.class "axis" :label "a"}}
    :d {:attributes {:label "d"}}
    :e {:attributes {:label "e"}}
    })

  ;; Apply stylesheet
  (ui/stylesheet gs/*current-graph* [[:node.axis {:fill-color "#00c"
                                                  :size "20px"}]
                                     [:node {:fill-color "green"}]
                                     [:node:clicked
                                      {:fill-color "red"}]
                                     [:edge {:arrow-shape "arrow"
                                             :arrow-size "10px, 5px"}]])
  
  

  ;; hara.object
  (object/access gs/*current-graph* :attributes {:ui.title "Hello World"})
  (object/access gs/*current-graph* :attributes)
  (object/access (object/access gs/*current-viewer* [:default-view :camera :metrics :high-point]))
  
  (object/to-data gs/*current-graph*) (type gs/*current-graph*)

  (object/access gs/*current-graph* [:attributes]))
