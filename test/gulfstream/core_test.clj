(ns gulfstream.core-test
  (:use midje.sweet)
  (:require [gulfstream.core :refer :all]))

^{:refer gulfstream.core/browse :added "0.1"}
(fact "returns a browser object for viewing and updating a graph. The browser includes 
a shadow dom so that any changes reflected within the shadow dom will be reflected in
the front end")


(comment

  (def view
    (browse {:title "Sample"

             :dom {:nodes {:a {:label "a"}
                           :b {:label "b"}
                           :c {:label "c"}}
                   :edges {[:a :b] {:label "a->b"
                                    :ui.class ["above"]}
                           [:b :a] {:label "b->a"
                                    :ui.class ["under"]}
                           [:b :c] {:label "b->c"
                                    :ui.class ["above"]}
                           [:c :b] {:label "c->b"
                                    :ui.class ["under"]}
                           [:c :a] {:label "c->a"
                                    :ui.class ["above"]}
                           [:a :c] {:label "a->c"
                                    :ui.class ["under"]}}}

             :style [[:node {:text-alignment "above"
                             :text-size "13px"}]
                     [:edge {:shape "cubic-curve"
                             :stroke-mode "dots"
                             :stroke-width "3px"
                             :fill-color "grey"}]
                     ["edge.under" {:text-alignment "under"
                                    :stroke-mode "dashes"}]
                     #_[:edge.above {:text-alignment "above"
                                   }]]
             
             :attributes {;;:gs.disable-auto-layout true
                          ;;:gs.disable-mouse true
                          }}))

  (view :title "Demo")
  

  (browse)

  )
