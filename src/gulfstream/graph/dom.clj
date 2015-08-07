(ns gulfstream.graph.dom
  (:require [gulfstream.util :as util]
            [hara.data.diff :as diff]
            [hara.object :as object]
            [hara.common.string :as s]
            [clojure.string :as string]))

(defn get-dom [graph]
  (let [data (concat (object/to-data (.getNodeSet graph))
                     (object/to-data (.getEdgeSet graph)))]
    (zipmap (map :id data) (map :attributes data))))

(defn element [graph k]
  (cond (or (string? k) (keyword? k) (symbol? k))
        (.getNode graph (s/to-string k))

        (vector? k)
        (.getEdge graph (->> (map s/to-string k)
                             (string/join "->")))))

(defn patch [graph diff]
  (doseq [[source target] (-> diff :edges :-)]
    (.removeEdge graph (str (s/to-string source) "->" (s/to-string target))))
  (doseq [k (-> diff :nodes :-)]
    (.removeNode graph (s/to-string k)))
  (doseq [k (-> diff :nodes :+)]
    (.addNode graph (s/to-string k)))
  (doseq [[source target] (-> diff :edges :+)]
    (let [source (s/to-string source)
          target (s/to-string target) ]
      (.addEdge graph (str source "->" target) source target true)))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (doseq [k (keys attrs)]
                   (.removeAttribute ele (s/to-string k)))
                 graph))
             graph
             (-> diff :attributes :-))
  (reduce-kv (fn [graph key attrs]
               (let [ele (element graph key)]
                 (reduce-kv (fn [ele ak av]
                              (doto ele (.setAttribute (s/to-string ak) (util/attribute-array av))))
                            ele
                            attrs)
                 graph))
             graph
             (-> diff :attributes :+)))

(defn make-patch-element [out key sign]
  (let [type (if (vector? key) :edges :nodes)]
    (update-in out [type sign] (fnil #(conj % key) #{}))))

(defn make-patch-attributes [out arr v sign]
  (let [arr (concat [:attributes sign] arr)]
    (println "PATCH:" arr)
    (case (count arr)
      3 (update-in out arr merge v)
      4 (assoc-in out arr v))))

(defn make-patch [changes]
  (let [ele-fn  (fn [output pos-sign diff-sign]
                  (reduce-kv (fn [out arr v]
                               (if (= 1 (count arr))
                                 (if (= :+ diff-sign)
                                   (-> out
                                       (make-patch-element (first arr) pos-sign)
                                       (make-patch-attributes arr v pos-sign))
                                   (make-patch-element out (first arr) pos-sign))
                                 (make-patch-attributes out arr v pos-sign)))
                             output
                             (diff-sign changes)))]
    (-> {}
        (ele-fn :+ :+)
        (ele-fn :+ :>)
        (ele-fn :- :-))))

(defn set-dom [graph dom]
  (->> (diff/diff dom (get-dom graph))
       (make-patch)
       (patch graph)))

(defn patch-dom [graph changes]
  (->> (make-patch changes)
       (patch graph)))
