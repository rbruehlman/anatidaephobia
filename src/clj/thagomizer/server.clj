(ns thagomizer.server
  (:require
   [taoensso.sente :as sente]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.reload :refer []]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [org.httpkit.server :as http-kit]
   [compojure.route    :as route]
   [compojure.core :as comp :refer (defroutes GET POST)]
   [thagomizer.handler.routes.ws :as ws-handler]
   [thagomizer.handler.routes.app :as app-handler]
   [thagomizer.handler.routes.messages :as msg-handler]
   [thagomizer.handler.routes.sns :as sns-handler]
   [thagomizer.handler.routes.visits :as visit-handler]
   [thagomizer.handler.routes.images :as image-handler]
   [thagomizer.ws :as ws])
  (:gen-class))

(defroutes ring-routes
  (GET "/" ring-req         (app-handler/landing-pg-handler ring-req))
  (GET "/chsk" ring-req     (ws/ring-ajax-get-or-ws-handshake ring-req))
  (POST "/chsk" ring-req    (ws/ring-ajax-post ring-req))
  (GET "/messages" ring-req (msg-handler/display-message-handler ring-req))
  (POST "/message" ring-req (msg-handler/new-message-handler ring-req))
  (GET "/draft" ring-req   (msg-handler/get-draft-handler ring-req))
  (POST "/draft" ring-req   (msg-handler/save-draft-handler ring-req))
  (POST "/sms" ring-req     (sns-handler/sns-handler ring-req))
  (POST "/visits" ring-req  (visit-handler/visit-handler ring-req))
  (POST "/images" ring-req  (image-handler/image-handler ring-req))
  (route/resources "/"      {:root "public"})
  (route/not-found          "<h1>Page not found</h1>"))

(defonce router_ (atom nil))

(def main-ring-handler
  (wrap-multipart-params
   (wrap-defaults ring-routes site-defaults)))

(defn stop-router! []
  (when-let [stop-fn @router_]
    (stop-fn)))

(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router!
           ws/ch-chsk ws-handler/event-msg-handler)))

(defonce web-server_ (atom nil))
(defn stop-web-server! []
  (when-let [stop-fn @web-server_]
    (stop-fn)))

(defn start-web-server! [& [port]]
  (stop-web-server!)
  (let [port (or port 8000)
        ring-handler (var main-ring-handler)
        [port stop-fn]
        (let [stop-fn (http-kit/run-server ring-handler {:port port})]
          [(:local-port (meta stop-fn)) (fn [] (stop-fn :timeout 100))])
        uri (format "http://0.0.0.0:%s/" port)]
    (println (str "Running on "  (format "http://0.0.0.0:%s/" port)))
    (try
      (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
      (catch java.awt.HeadlessException _))

    (reset! web-server_ stop-fn)))

(defn stop! []
  (stop-router!)
  (stop-web-server!))

(defn start! []
  (start-router!)
  (start-web-server!)
  )

(defn -main []
  (start!))