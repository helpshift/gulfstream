(defproject helpshift/gulfstream "0.2.1"
  :description "graphing library wrapper built on top of graphstream"
  :url "https://www.github.com/helpshift/gulfstream"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [garden "1.2.5"]
                 [net.sourceforge.cssparser/cssparser "0.9.16"]
                 [im.chit/hara.object "2.2.11"]
                 [im.chit/hara.data.diff "2.2.11"]
                 [org.graphstream/gs-ui "1.3"]]
  :profiles {:dev {:dependencies [[midje "1.7.0"]
                                  [helpshift/hydrox "0.1.3"]]
                  :plugins [[lein-midje "3.1.3"]]}}

  :documentation {:site   "gulfstream"
                  :output "docs"
                  :template {:path "template"
                             :copy ["assets"]
                             :defaults {:template     "article.html"
                                        :navbar       [:file "partials/navbar.html"]
                                        :dependencies [:file "partials/deps-web.html"]
                                        :navigation   :navigation
                                        :article      :article}}
                  :paths ["test/documentation"]
                  :files {"index"
                          {:input "test/documentation/gulfstream_guide.clj"
                           :title "gulfstream"
                           :subtitle "rapid graph visualizations"}}}

  )
