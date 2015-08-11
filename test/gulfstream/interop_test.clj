(ns gulfstream.interop
  (:use midje.sweet))

^{:refer gulfstream.interop/get-attributes :added "0.1"}
(fact "gets the attribute of a graph, node or element as a map")

^{:refer gulfstream.interop/set-attributes :added "0.1"}
(fact "sets the attribute of a graph, node or element in the form of a map")
