(ns gulfstream.graph.css
  (:require [hara.object :as object]
            [garden.core :as css]
            [gulfstream.util :as util]
            [clojure.string :as string])
  (:import [com.steadystate.css.parser CSSOMParser]
           [org.w3c.css.sac InputSource]
           [java.io StringReader]))

(defn property-pair
  "splits up a property pair into a vector for input into a map
 
   (property-pair \"hello : world\")
   => [:hello \"world\"]
   "
  {:added "0.1"}
  [text]
  (let [props (-> text .toString (string/split #": "))]
    (-> props
        (update-in [0] (comp keyword string/trim)))))

(defn rule-pair [rule]
  (let [k  (->> rule
                .getSelectors
                .getSelectors
                (map str))
        k  (if (= 1 (count k))
             (-> k first keyword)
             (string/join " " k))
        v  (->> (.getProperties (.getStyle rule))
                (map property-pair)
                (into {}))]

    [k v]))

(defn parse
  "transforms a css-string into a clojure datastructure
 
   (parse \"node {\\n  shape: freeplane;\\n}\")
   => [[:node {:shape \"freeplane\"}]]"
  {:added "0.1"}
  [st]
  (let [stream (StringReader. st)
       source  (InputSource. stream)
       parser  (CSSOMParser.)
       stylesheet (.parseStyleSheet parser source nil nil)
       rule-list (.getRules (.getCssRules stylesheet))]
   (mapv rule-pair rule-list)))

(defn emit
  "transforms a clojure datastructure into a css string
   
   (emit [[:node {:shape \"freeplane\"}]])
   => (str \"node {\\n  shape: freeplane;\\n}\")"
  {:added "0.1"}
  [v]
  (string/join "\n" (map css/css v)))

(defn get-stylesheet
  "accessor function for graph stylesheet property
 
   (-> (graph/graph {:style [[:node {:color \"red\"}]]})
       (get-stylesheet))
   => [[:node {:color \"red\"}]]
   "
  {:added "0.1"}
  [graph]
  (if-let [css (.getAttribute graph "ui.stylesheet")]
    (parse css)))

 (defn set-stylesheet
  "setter function for graph stylesheet property
   
   (-> (graph/graph {})
       (set-stylesheet [[:node {:color \"red\"}]])
       (get-stylesheet))
   => [[:node {:color \"red\"}]]"
  {:added "0.1"}
  [graph stylesheet]
   (if stylesheet
        (.setAttribute graph "ui.stylesheet"
                       (util/attribute-array (emit (seq stylesheet)))))
   graph)
