(ns gulfstream.util
  (:use midje.sweet))

^{:refer gulfstream.util/attribute-array :added "0.1"}
(fact "creates a datastructure compatible with the call to setAttribute"

  (seq (attribute-array [0 1 2 3 4]))
  => [0 1 2 3 4]

  (seq (attribute-array "hello"))
  => ["hello"])
