(defproject thagomizer "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.758"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.10.13"]   ;; Build tooling
                 [reagent "1.0.0-alpha2"]           ;; Interface to React
                 [stylefy "2.2.1"]                  ;; Styling lib
                 [re-com "2.8.0"]                   ;; UI library
                 [day8.re-frame/test "0.1.5"]       ;; Testing framework
                 [day8.re-frame/http-fx "v0.2.0"]   ;; http-fx handler
                 [cljsjs/moment "2.24.0-0"]         ;; Date parsing
                 [re-frame "1.0.0-rc5"]             ;; State, effect, and subscription pattern
                 [compojure "1.6.1"]                ;; Routing library
                 [http-kit "2.3.0"]                 ;; Server
                 [ring "1.8.0"]
                 [com.taoensso/sente "1.15.0"]      ;; websocket
                 [ring                      "1.8.0"]
                 [ring/ring-defaults        "0.3.2"]
                 [ring/ring-anti-forgery "1.3.0"]]

  :plugins [[lein-shell "0.5.0"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :main thagomizer.core
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}
  :aliases {"dev"          ["with-profile" "dev" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]]
            "prod"         ["with-profile" "prod" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]}
  :profiles {:dev {:dependencies [[binaryage/devtools "1.0.0"]
                                  [day8.re-frame/re-frame-10x "0.6.0"]]
                   :source-paths ["frontend-dev"]}
             :prod {}})
