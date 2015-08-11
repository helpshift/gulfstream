(ns gulfstream.util)

(defn attribute-array
  "creates a datastructure compatible with the call to setAttribute
 
   (seq (attribute-array [0 1 2 3 4]))
   => [0 1 2 3 4]
 
   (seq (attribute-array \"hello\"))
   => [\"hello\"]"
  {:added "0.1"}
  [v]
  (cond (vector? v)
        (object-array v)

        :else
        (object-array [v])))
