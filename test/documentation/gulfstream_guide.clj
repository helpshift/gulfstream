(ns documentation.gulfstream-guide
  (:require [midje.sweet :refer :all]))

[[:chapter {:title "Introduction"}]]

[[:section {:title "Overview"}]]

"[gulfstream](https://github.com/helpshift/gulfstream) leverages [graphstream](http://graphstream-project.org/) to allow for easy to use visualisations of data."

[[:section {:title "Installation"}]]

"In your project.clj, add gulfstream to the `[:dependencies]` entry:

```clojure
(defproject ...
    ...
    :dependencies [...
                   [helpshift/gulfstream \"{{PROJECT.version}}\"]
                   ...]}}
    ...)
```
"

"All functionality is the `gulfstream.core` namespace:"

(comment
  (require '[gulfstream.core :as gs]))

[[:chapter {:title "Quickstart"}]]

[[:section {:title "Simple Graph"}]]

"Lets go ahead and draw the most simple graph we possibly could:"

(comment
  (def browser
    (gs/browse {:title "Simple"
                :dom {:nodes {:a {:label "a"}
                              :b {:label "b"}}
                      :edges {[:a :b] {:label "a->b"}}}})))

"We should see a window popping up as follows:"

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9682239/5abe94be-5322-11e5-9bec-d1f82b0c7f13.png"
          :width "100%"}]]

[[:section {:title "Styling"}]]

"The window shows a label for the edge `a->b` but the labels `a` and `b` are hidden. We can expose it by adjusting the style of the browser:"

(comment
  (browser :style [[:node {:text-alignment :under}]]))

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9682243/5d72768a-5322-11e5-886c-b7ec4b5e89f4.png"
          :width "100%"}]]

"We can change the look of our graph some more by specifying more styles:"

(comment
  (browser :style [[:node {:fill-color :green
                           :size "20px"
                           :text-size "15px"
                           :text-alignment :under}]
                   [:edge {:text-size "15px"
                           :stroke-mode "dashes"}]]))

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9682359/9f3a7620-5323-11e5-86ae-6cd4250b55f8.png"
          :width "100%"}]]

[[:section {:title "Properties"}]]

"We can see what properties are accessible by calling browser with no arguments:"

(comment
  (browser)
  => {:getters (:title :style :attributes :dom)
      :setters (:title :style :attributes :dom)})
                                        
"It can be seen that there are four properties that we can apply getters and setters to. For instance, we can access the `:title` like this:"

(comment
  (browser :title)
  => "Simple")

"And we can change the title as follows:"

(comment
  (browser :title "Hello There"))

"Which will update the title of our graph"

(comment
  (browser :title)
  => "Hello There")

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9682454/65f83a5e-5324-11e5-8213-961906f1dd1b.png"
          :width "100%"}]]

[[:section {:title "DOM access"}]]

"Similarily, we can access the dom by calling browser with the `:dom` key:"

(comment
  (browser :dom)
  => {:nodes {:a {:label "a"}
              :b {:label "b"}},
      :edges {[:a :b] {:label "a->b"}}})

"We can also set dom elements as follows:"

(comment
  (browser :dom
           {:nodes {:a {:label "a"}
                    :b {:label "b"}
                    :c {:label "c"}},
            :edges {[:a :b] {:label "a->b"}
                    [:b :c] {:label "b->c"}
                    [:c :a] {:label "c->a"}}}))

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9682544/47114008-5325-11e5-8b92-5d9c3c11968f.png"
          :width "100%"}]]

[[:section {:title "More Customisations"}]]

"We can do more customisations on our graph by adding edges as well as changing shapes:"

(comment
  (browser :dom
           {:nodes {:a {:label "a"
                        :ui.class ["red" "large"]}
                    :b {:label "b"
                        :ui.class ["green"]}
                    :c {:label "c"
                        :ui.class ["large" "blue"]}}
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
                             :ui.class ["under"]}}})
  
  (browser :style [[:node {:text-size "15px"
                           :text-alignment :under
                           :size "15px"}]
                   [:node.large {:size "30px"}]
                   [:node#a {:shape "cross"}]
                   [:node#b {:shape "diamond"}]
                   [:node#c {:shape "box"}]
                   [:node.green {:fill-color "green"}]
                   [:node.red   {:fill-color "red"}]
                   [:node.blue  {:fill-color "blue"}]
                   [:edge {:shape "cubic-curve"}]
                   [:edge.under {:text-alignment "under"
                                 :fill-color "red"}]
                   [:edge.above {:text-alignment "above"
                                 :fill-color "green"}]]))

[[:image {:src "https://cloud.githubusercontent.com/assets/1455572/9683055/0572ee76-532a-11e5-9d16-b797f15fb4b5.png"
          :width "100%"}]]


[[:chapter {:title "Examples"}]]

"More examples to [come](http://openflights.org/data.html).
 - [todo](http://personality-testing.info/_rawdata/)
 - [todo 2](http://arcane-coast-3553.herokuapp.com/snas/3)
"
