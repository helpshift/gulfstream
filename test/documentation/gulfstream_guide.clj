(ns documentation.gulfstream-guide
  (:require [midje.sweet :refer :all]))

[[:chapter {:title "Introduction"}]]

[[:section {:title "Overview"}]]

"[gulfstream](https://github.com/helpshift/gulfstream) leverages [graphstream](http://graphstream-project.org/) to allow for easy to use visualisations of data."

[[:section {:title "Installation"}]]

"In your project.clj, add gulfstream to the `[:dependencies]` entry:

```clojure
(defproject ...
    ...
    :dependencies [...
                   [helpshift/gulfstream \"{{PROJECT.version}}\"]
                   ...]}}
    ...)
```
"

"All functionality is the `gulfstream.core` namespace:"
