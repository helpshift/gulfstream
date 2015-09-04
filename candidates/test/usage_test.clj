(ns gulfstream.usage-test
  (:require [gulfstream.core :as gs]
            [gulfstream.graph :as graph]))

(require '[gulfstream.core :refer :all])

(-> (gs/graph
     {:attributes {:layout.quality 1.0 :layout.force 0.8}
      :dom (gs/expand {:links
                       {:poverty #{:mental-health :meals :avaliability :vegetable :government-support :stress}
                        :government-support #{:financial-stablity}
                        :financial-stablity #{:stress}
                        :jobs #{:financial-stablity :poverty}
                        :meals #{:vegetable :meals}
                        :kids-off-streets #{:safety}
                        :marketing #{:avaliability}
                        :vending-machines #{:avaliability}
                        :avaliability #{:social-disorder}
                        :social-disorder #{:safety}}
                       :elements
                       {:social-disorder    {:label "Social Disorder"}
                        :avaliability       {:label "Avaliability of Junk Food"}
                        :poverty            {:label "Poverty"}
                        :stress             {:label "Stress"}
                        :safety             {:label "Perceived Neighbourhood Safety"}
                        :financial-stablity {:label "Financial Stablity"}
                        :jobs               {:label "Jobs"}
                        :kids-off-streets   {:label "Kids off the Streets"}
                        :government-support {:label "Government Support"}
                        :meals              {:label "Healthy Meals per Day"}
                        :mental-health      {:label "Mental Health"}
                        :marketing          {:label "Unhealthy Marketing"}
                        :vending-machines   {:label "Vending Machines"}
                        :vegetable          {:label "Fruit and Vegetable"}}})})
    (gs/display))



(gs/screenshot gs/*current-graph* "poverty3.png")

(-> (gs/graph
     {:attributes {:layout.quality 1.0 :layout.force 0.8}
      :dom (gs/expand {:links
                       {:poverty #{:mental-health :meals :avaliability :vegetable :government-support :stress}
                        :government-support #{:financial-stablity}
                        :financial-stablity #{:stress}
                        :jobs #{:financial-stablity :poverty}
                        :meals #{:vegetable :meals}
                        :kids-off-streets #{:safety}
                        :marketing #{:avaliability}
                        :vending-machines #{:avaliability}
                        :avaliability #{:social-disorder}
                        :social-disorder #{:safety}}
                       :elements
                       {:social-disorder    {:label "Social Disorder"}
                        :avaliability       {:label "Avaliability of Junk Food"
			                     :ui.class ["big" "red"]}
                        :poverty            {:label "Poverty"
			                     :ui.class ["big" "green"]}
                        :stress             {:label "Stress"}
                        :safety             {:label "Perceived Neighbourhood Safety"}
                        :financial-stablity {:label "Financial Stablity"}
                        :jobs               {:label "Jobs"
			                     :ui.class "green"}
                        :kids-off-streets   {:label "Kids off the Streets"}
                        :government-support {:label "Government Support"}
                        :meals              {:label "Healthy Meals per Day"}
                        :mental-health      {:label "Mental Health"}
                        :marketing          {:label "Unhealthy Marketing"}
                        :vending-machines   {:label "Vending Machines"}
                        :vegetable          {:label "Fruit and Vegetable"
                                             :ui.class "green"}}})
      :style [["node.green" {:fill-color "green"}]
              ["node.red"   {:fill-color "red"}]
              ["node.big"   {:size "20px"}]]})
    (gs/display))
