(ns thagomizer.router
  (:require
   [ring.middleware.defaults]
   [compojure.route    :as route]
   [thagomizer.handler :as handler]
   [thagomizer.server :as server]
   [compojure.core :as comp :refer (defroutes GET POST)]))

(defroutes ring-routes
  (GET "/" ring-req         (handler/landing-pg-handler ring-req))
  (GET "/chsk" ring-req     (server/ring-ajax-get-or-ws-handshake ring-req))
  (POST "/chsk" ring-req    (server/ring-ajax-post ring-req))
  (route/resources "/"      {:root "public/js"})
  (route/not-found          "<h1>Page not found</h1>"))