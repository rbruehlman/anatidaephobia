(ns thagomizer.handler.routes.app
  (:require
   [ring.middleware.anti-forgery :as anti-forgery]
   [hiccup.page :refer [html5]]))

(defn landing-pg-handler [ring-req]
  (html5 {:ng-app "Thagomizer" :lang "en"}
         [:head
          [:title "The Thagomizer"]
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          [:link {:href "https://fonts.googleapis.com/css2?family=Gloria+Hallelujah&display=swap"
                  :rel "stylesheet"}]
          [:link {:href "https://cdn.jsdelivr.net/npm/bulma@0.9.0/css/bulma.min.css"
                  :rel "stylesheet"}]
          [:style "body {margin:0; padding:0}"]] ;; I HAVE TRIUMPHED, CSS.  hate you hate you hate you
         [:body
          [:div {:class "container"}
           (let [csrf-token
                 (force anti-forgery/*anti-forgery-token*)]
             [:div#sente-csrf-token {:data-csrf-token csrf-token}])
           [:div#app]
           [:script {:src "main.js"}]]]))

