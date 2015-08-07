(ns gulfstream.graph.css
  (:require [hara.object :as object]
            [garden.core :as css])
  (:import [com.steadystate.css.parser CSSOMParser]
           [org.w3c.css.sac InputSource]
           [java.io StringReader]))

(defn property-pair [text]
  (let [props (-> text .toString (clojure.string/split #": "))]
    (-> props
        (update-in [0] keyword))))

(defn rule-pair [rule]
  (let [k  (->> rule
                .getSelectors
                .getSelectors
                (map str))
        k  (if (= 1 (count k))
             (-> k first keyword)
             (clojure.string/join " " k))
        v  (->> (.getProperties (.getStyle rule))
                (map property-pair)
                (into {}))]

    [k v]))

(defn parse [st]
  (let [stream (StringReader. st)
       source  (InputSource. stream)
       parser  (CSSOMParser.)
       stylesheet (.parseStyleSheet parser source nil nil)
       rule-list (.getRules (.getCssRules stylesheet))]
   (mapv rule-pair rule-list)))

(defn emit [v]
  (apply css/css v))

(defn get-stylesheet
  [graph]
  (if-let [css (.getAttribute graph "ui.stylesheet")]
    (css/parse css)))

 (defn set-stylesheet
   [graph stylesheet]
   (if stylesheet
        (.setAttribute graph "ui.stylesheet"
                       (interop/attribute-array (css/emit (seq stylesheet))))))

(comment
  (-> [[:edge {:arrow-shape "arrow"
               :arrow-size "10, 10"}]
       [:node {:shape "freeplane"}]]
      emit
      parse)
  => [[:edge {:arrow-size "10, 10", :arrow-shape "arrow"}]
      [:node {:shape "freeplane"}]])
