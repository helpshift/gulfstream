(ns gulfstream.util)

(defn attribute-array [v]
  (cond (vector? v)
        (object-array v)

        :else
        (object-array [v])))
