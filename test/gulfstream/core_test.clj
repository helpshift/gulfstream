(ns gulfstream.core-test
  (:use midje.sweet))

^{:refer gulfstream.core/browse :added "0.1"}
(fact "returns a browser object for viewing and updating a graph. The browser includes 
a shadow dom so that any changes reflected within the shadow dom will be reflected in
the front end")
