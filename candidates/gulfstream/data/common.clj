(ns gulfstream.data.common)

(defn attribute-array [v]
  (cond (vector? v)
        (object-array v)

        :else
        (object-array [v])))
