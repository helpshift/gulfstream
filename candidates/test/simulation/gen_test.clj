(ns gulfstream.simulation.gen-test
  (:require [hara.data.diff :as diff]))


(comment
  (diff/diff
   {[:b :a] {:attributes {:ui.class "link"}}, [:b :c] {}, :c {:attributes {:ui.class "axis"}, :degree 1}, :b {:degree 2}, :a {:attributes {:ui.class "axis"}, :degree 1}}
   {[:b :a] {}, [:b :c] {}, :c {:attributes {:ui.class "axis"}, :degree 1}, :b {:degree 2}, :a {:attributes {:ui.class "axis"}, :degree 1}})

  (diff/diff
   {[:b :a] {}}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {},
      :- {[[:b :a] :attributes] {:ui.class "link"}},
      :+ {}}

  {:nodes {:+ [:b :c] :- []}
   :edges {:+ [[:b :c]] :- [[:c :a]]}
   :attributes {:+ {[:b :c] {:ui.class "axis"}}
                :- {[:b :c] {:ui.flip  true}}}}

  (diff/diff
   {}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {}, :- {[[:b :a]] {:attributes {:ui.class "link"}}}, :+ {}}

  (diff/diff
   {[:b :a] {:attributes {:ui.class "hello"}}}
   {[:b :a] {:attributes {:ui.class "link"}}})
  => {:> {[[:b :a] :attributes :ui.class] "hello"},
      :- {},
      :+ {}}
  )
