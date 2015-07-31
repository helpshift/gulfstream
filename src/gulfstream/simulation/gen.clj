(ns gulfstream.simulation.gen
  (:require [clojure.string :as string]
            [hara.string.case :as case]
            [hara.reflect :as reflect])
  (:import [org.graphstream.algorithm.generator
            BananaTreeGenerator
            BarabasiAlbertGenerator
            ChainGenerator
            ChvatalGenerator
            DorogovtsevMendesGenerator
            FlowerSnarkGenerator
            FullGenerator
            GridGenerator
            HypercubeGenerator
            IncompleteGridGenerator
            LCFGenerator
            LifeGenerator
            LobsterGenerator
            PetersenGraphGenerator
            PointsOfInterestGenerator
            RandomEuclideanGenerator
            RandomFixedDegreeDynamicGraphGenerator
            RandomGenerator
            URLGenerator
            WattsStrogatzGenerator
            WikipediaGenerator]))

(defn slug [gen]
  (-> (.getName gen)
      (string/split #"\.")
      last
      (.replace "Generator" "")
      (case/spear-case)
      keyword))

(defonce +generator-map+
  (let [gen (create-ns 'gulfstream.simulation.gen)
        cls (->> (.getMappings gen)
                 (reduce-kv (fn [out k v]
                              (if (and (class? v)
                                       (.startsWith (.getName v) "org.graphstream.algorithm.generator"))
                                (assoc out (slug v)
                                       (reflect/query-class v ["new" :#]))
                                out))
                            {}))]
    cls))

(defn generator
  ([] (keys +generator-map+))
  ([k] ((get +generator-map+ k)))
  ([k args]
   (apply (get +generator-map+ k) args)))

(defn hook
  ([graph] (hook graph {}))
  ([graph {:keys [type args steps interval] :as config}]
   (if (and config type)
     (let [gen (generator type args)]
       (.addSink gen graph)
       (.begin gen)
       (future (dotimes [_ steps]
                 (Thread/sleep interval)
                 (.nextEvents gen)))))
   graph))

(comment
  :generator {:type :barabasi-albert
              :args []
              :steps 1000
              :interval 100})
