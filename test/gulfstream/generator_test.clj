(ns gulfstream.generator-test
  (:require [gulfstream.core :as gs]
            [gulfstream.generators :as gen]))

(comment
  (-> (gs/graph {:ui {:title "Barabasi"}
                 :generator {:type :barabasi-albert
                             :args []
                             :steps 100
                             :interval 100}})
      (gs/display))

  )

(-> (gs/graph {:ui {:title "Barabasi"}})
    (gs/display)
    )



