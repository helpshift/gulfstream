(ns gulfstream.generator
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

(defn generator-slug [gen]
  (-> (.getName gen)
      (string/split #"\.")
      last
      (.replace "Generator" "")
      (case/spear-case)
      keyword))

(defonce +generator-map+
  (let [gen (create-ns 'gulfstream.generator)
        cls (->> (.getMappings gen)
                 (reduce-kv (fn [out k v]
                              (if (and (class? v)
                                       (.startsWith (.getName v) "org.graphstream.algorithm.generator"))
                                (assoc out (generator-slug v)
                                       (reflect/query-class v ["new" :#]))
                                out))
                            {}))]
    cls))

(defn generator
  ([] (keys +generator-map+))
  ([k] ((get +generator-map+ k)))
  ([k args]
   (apply (get +generator-map+ k) args)))

(defn hook [graph {:keys [type args steps interval] :as config}]
  (let [gen (generator type args)]
    (.addSink gen graph)
    (.begin gen)
    (future (dotimes [_ steps]
              (Thread/sleep interval)
              (.nextEvents gen)))
    graph))

(comment
  :generator {:type :barabasi-albert
              :args []
              :steps 1000
              :interval 100})
