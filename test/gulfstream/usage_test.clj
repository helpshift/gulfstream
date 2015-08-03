(ns gulfstream.usage-test
  (:require [gulfstream.core :as gs]))

(gs/display
 (gs/graph
  {:attributes {:layout.quality 3.5}
   :dom (gs/expand {
                    :links    {:poverty #{:mental-health :meals :avaliability :vegetable :government-support :stress}
                               :government-support #{:financial-stablity}
                               :financial-stablity #{:stress}
                               :jobs #{:financial-stablity :poverty}
                               :meals #{:vegetable :meals}
                               :kids-off-streets #{:safety}}
                    :elements {:social-disorder {:label "Social Disorder"}
                               :avaliability    {:label "Avaliability of Junk Food"}
                               :poverty         {:label "Poverty"}
                               :stress          {:label "Stress"}
                               :safety          {:label "Perceived Neighbourhood Safety"}
                               :financial-stablity {:label "Financial Stablity"}
                               :jobs            {:label "Jobs"}
                               :kids-off-streets {:lable "Kids off the Streets"}
                               :government-support {:label "Government Support"}
                               :meals           {:label "Healthy Meals per Day"}
                               :mental-health   {:label "Mental Health"}
                               :marketing       {:label "Unhealthy Marketing"}
                               :vending-machines {:label "Vending Machines"}
                               :vegetable {:label "Fruit and Vegetable"}}})}))
