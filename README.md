# gulfstream

wickedly simple graph visualization

## Installation

Add to `project.clj`:

```clojure
[helpshift/gulfstream "0.1.6"]
```

Functionality is in the core namespace:

```clojure
(require '[gulfstream.core :as gs])
```

## Quickstart

[gulfstream]() leverages [graphstream](http://graphstream-project.org/) to allow for easy to use visualisations of data. Lets draw the most simple graph we possibly could:

```clojure
(def browser (gs/browse {:title "Simple"
                           :dom {:a {:label "a"}
                                 :b {:label "b"}
                                 [:a :b] {:label "a->b"}}}))
```

We should see a window popping up as follows:

![simple](https://cloud.githubusercontent.com/assets/1455572/9192352/fab7bf3a-4027-11e5-8fd6-19d4f5d9d4d5.png)

We can see what properties are accessible by calling browser with no arguments:

```clojure
(browser)
;=> {:getters (:attributes :style :dom :title), :setters (:attributes :style :dom :title)}
```

For instance, we can access the title like this:

```clojure
(browser :title "Hello There")
;=> "Simple"
```

And we can change the title as follows:

```clojure
(browser :title "Hello There")
```

![change title](https://cloud.githubusercontent.com/assets/1455572/9192531/94734b02-4029-11e5-81e7-09696d775116.png)

### Changing DOM

We can change elements that we want to display by placing a new dom element. This time, we use a map instead of a key value pair to update our browser:

```clojure
(browser {:title "Triangle"
          :dom {:a {:label "a"}
                :b {:label "b"}
                :c {:label "c"}
                [:a :b] {:label "a->b" :ui.class "sinking"}
                [:b :c] {:label "b->c"}
                [:c :a] {:label "c->a"}}})
```

![dom manipulation](https://cloud.githubusercontent.com/assets/1455572/9192577/d64d3d58-4029-11e5-93a7-0dc5813ea5e8.png)

### Styling DOM

Note the use of `:ui.class` on the `[:a :b]` edge. This is exactly the same concept as rendering html. We can now style our browser using the following call:

```clojure
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
                   :arrow-size "10px, 10px"}]])
```

The result can be seen as follows:

![styling](https://cloud.githubusercontent.com/assets/1455572/9192614/30473fe8-402a-11e5-8275-2b48cb86f953.png)

## License

Copyright © 2015 Helpshift Pty Ltd

Distributed under the Eclipse License
