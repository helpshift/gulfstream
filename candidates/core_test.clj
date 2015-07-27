(ns gulfstream.core-test
  (:require [gulfstream.core :as graph]
            [rhizome.viz :as viz]
            [rhizome.dot :as dot]
            ))


(comment

  (defn select-decendents
    ([nodes entry]
     (select-decendents nodes entry {}))
    ([nodes entry out]
     (let [adjacent (get nodes entry)]
       (reduce (fn [out k]
                 (if-let [val (get out k)]
                   out
                   (select-decendents nodes k
                                      (assoc out k (get nodes k)))))
               (assoc out entry adjacent)
               adjacent))))

  (defn view-graph [items]
    (viz/view-graph (keys items) #(get items % [])
                    :node->descriptor (fn [n] {:label n})))
  
  (get items 'moby.util.core/iffn)
  


  (view-graph (select-decendents items 'www.xhr.issue/get-smart-view-builder))
  
  (view-graph (select-decendents items 'www.xhr.user/list-agents))
  
  (view-graph (select-decendents items 'api.lib.handlers.issue/process-customer-survey))
  
  
  
  (select-decendents {"A" #{"B"}
                      "B" #{"C"}
                      "C" #{}}
                     "A")

  (spit )

  
  
  (graph/graph contents)

  (graph/display
   (graph/graph contents))

  (viz/view-graph (keys items) items)
  (def dot (dot/graph->dot (keys items) items
                           :node->descriptor (fn [n] {:label n})))

  (future
    (spit "sample.svg" (viz/dot->svg dot)))

  (def thd *1)
  (def svg )

  (viz/graph->svg (keys items) items)
  ()
  
  (viz/view-graph ["A" "B" "C"] {"A" ["B"]
             "B" ["C"]
             "C" ["A"]})
  
  (def contents
    {:type :single
     :title "Test Graph"
     :style "node{}"
     :items {"A" ["B"]
             "B" ["C"]
             "C" ["A"]}})
  
  (def items (read-string (slurp "../moby/deps.edn")))

  (count items)

  
  (graph/display
   (graph/graph (assoc contents
                       :items items)))
  )
