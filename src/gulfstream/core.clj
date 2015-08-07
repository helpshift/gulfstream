(ns gulfstream.core
  (:require [gulfstream.graph :as graph]
            [gulfstream.graph.dom :as dom]
            [hara.data.diff :as diff]
            [hara.object :as object]))

(defonce +current+ nil)

(defrecord Browser [])

(defmethod print-method Browser
  [v w]
  (.write w (str (into {} v))))

(defn browse [{:keys [dom style attributes options listeners]}]
  (let [graph   (graph/graph {:dom dom :style style :attributes attributes})
        shadow  (atom dom)
        {:keys [keyboard node]} listeners
        viewer  (graph/display graph options)
        viewer  (if node
                  (graph/add-node-listener viewer node)
                  viewer)
        viewer  (if keyboard
                  (graph/add-key-listener viewer keyboard)
                  viewer)
        browser (map->Browser {:graph  graph
                               :dom    shadow
                               :viewer viewer})]
    (add-watch shadow :pipeline
               (fn [_ _ p n]
                 (dom/patch-dom graph (diff/diff n p)))) 
    (alter-var-root #'+current+ (constantly browser))
    browser))

(object/extend-maplike

 Browser
 {:tag "browser"
  :default false
  :getters {:attributes #(object/access (:graph %) :attributes)
            :style      #(object/access (:graph %) :style)
            :dom        #(-> % :dom deref)}
  :setters {:attributes #(object/access (:graph %1) :attributes %2)
            :style      #(object/access (:graph %1) :style %2)
            :dom        #(reset! (:dom %1) %2)}})

(comment
  (browse {:dom {:a {:label "a"}
                 :b {:label "b"}
                 [:a :b] {}}})

  (object/access +current+ :dom {:c {:label "c"}
                                 :b {:label "b"}
                                 :a {:label "a" :ui.class ["selected"]}
                                 [:c :a] {}
                                 [:b :c] {}})
  

  (reset! (:dom +current+)  {:c {:label "c"}
                             :a {:label "a" :ui.class ["selected"]}
                             [:c :a] {}})

  (object/access (:graph +current+) :dom)
  ;;{:getters (:node-set :edge-set :strict? :index :step :attributes :dom :style), :setters (:index :attributes :dom :style)}

  (object/access (:graph +current+) :style [[:node.selected {:fill-color "red"
                                                             :size "20px"}]])


  #_(def keyboard
      {KeyEvent/VK_ESCAPE  :escape
       KeyEvent/VK_ENTER   :enter
       KeyEvent/VK_SPACE   :space
       KeyEvent/VK_SHIFT   :shift
       KeyEvent/VK_CONTROL :control
       KeyEvent/VK_META    :meta
       KeyEvent/VK_ALT     :alt}))

