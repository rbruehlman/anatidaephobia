(defproject thagomizer "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.758"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.10.13"] 
                 [reagent "1.0.0-alpha2"]
                 [day8.re-frame/http-fx "v0.2.0"]
                 [cljsjs/moment "2.24.0-0"]
                 [re-frame "1.1.2"]
                 [compojure "1.6.1"] 
                 [http-kit "2.3.0"]
                 [ring "1.8.0"]
                 [com.taoensso/sente "1.15.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-anti-forgery "1.3.0"]
                 [com.cognitect.aws/api "0.8.474"]
                 [com.cognitect.aws/sns "807.2.729.0"]
                 [com.cognitect.aws/s3 "810.2.801.0"]
                 [com.cognitect.aws/endpoints "1.1.11.842"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [org.postgresql/postgresql "42.2.18"]
                 [ring/ring-devel "1.8.2"]
                 [cheshire "5.10.0"]
                 [com.github.gkarthiks/s3-presigned-url "0.1.1"]]

  :plugins [[lein-shell "0.5.0"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]
  :clean-targets ^{:protect false} ["resources/public" "target"]
  :main ^:skip-aot thagomizer.server
  :aot [:all]
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}
  :aliases {"dev"          ["with-profile" "frontend-dev" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]]
            "prod"         ["with-profile" "frontend-prod" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]}
  :profiles {:frontend-dev {:dependencies [[binaryage/devtools "1.0.0"]
                                           [day8.re-frame/re-frame-10x "0.6.0"]]}
             :frontend-prod {}
             :uberjar
              {:prep-tasks ["compile"
                            ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]
               :aot :all
               :uberjar-name "thagomizer.jar"}})