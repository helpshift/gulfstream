(ns gulfstream.usage-test
  (:require [gulfstream.core :as gs]
            [gulfstream.graph :as graph]
            [hara.object :as object]))

(comment
  (def browser (gs/browse {:title "Simple"
                           :dom {:a {:label "a"}
                                 :b {:label "b"}
                                 [:a :b] {:label "a->b"}}}))

  (browser)
  => {:getters (:attributes :style :dom :title), :setters (:attributes :style :dom :title)}

  
  (browser :title)
  
  (browser {:title "Triangle"
            :dom {:a {:label "a"}
                  :b {:label "b"}
                  :c {:label "c"}
                  [:a :b] {:label "a->b" :ui.class "sinking"}
                  [:b :c] {:label "b->c"}
                  [:c :a] {:label "c->a"}}})

  (browser :node)

  (browser :style [["node#a" {:fill-color "green"
                              :text-size "20px"
                              :size  "30px"}]
                   ["node#b" {:fill-color "red"
                              :text-size "20px"
                              :size  "20px"}]
                   ["node" {:fill-color "black"
                            :size  "10px"
                            :shape "box"
                            :text-size "10px"}]
                   ["edge" {:fill-color "gray"
                            :arrow-size "5px, 5px"}]
                   ["edge.sinking"
                    {:fill-color "blue"
                     :arrow-size "10px, 10px"}]]))
