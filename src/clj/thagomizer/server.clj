(ns thagomizer.server
  (:require
   [taoensso.sente :as sente]
   [ring.middleware.defaults]
   [org.httpkit.server :as http-kit]
   [compojure.core :as comp :refer (defroutes GET POST)]
   [compojure.route    :as route]
   [thagomizer.handler :as handler]
   [thagomizer.ws :as ws])
  (:gen-class))

(defroutes ring-routes
  (GET "/" ring-req         (handler/landing-pg-handler ring-req))
  (GET "/chsk" ring-req     (ws/ring-ajax-get-or-ws-handshake ring-req))
  (POST "/chsk" ring-req    (ws/ring-ajax-post ring-req))
  (POST "/sms" ring-req     (handler/sms-handler ring-req))
  (route/resources "/"      {:root "public"})
  (route/not-found          "<h1>Page not found</h1>"))

(defonce router_ (atom nil))

(def main-ring-handler
  (ring.middleware.defaults/wrap-defaults
   ring-routes ring.middleware.defaults/site-defaults))

(defn stop-router! []
  (when-let [stop-fn @router_]
    (stop-fn)))

(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router!
           ws/ch-chsk handler/event-msg-handler)))

(defonce web-server_ (atom nil))
(defn stop-web-server! []
  (when-let [stop-fn @web-server_]
    (stop-fn)))

(defn start-web-server! [& [port]]
  (stop-web-server!)
  (let [port (or port 5000)
        ring-handler (var main-ring-handler)
        [port stop-fn]
        (let [stop-fn (http-kit/run-server ring-handler {:port port})]
          [(:local-port (meta stop-fn)) (fn [] (stop-fn :timeout 100))])
        uri (format "http://0.0.0.0:%s/" port)]
    (println (str "Running on "  (format "http://0.0.0.0:%s/" port))
)
    (try
      (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
      (catch java.awt.HeadlessException _))

    (reset! web-server_ stop-fn)))

(defn stop! []
  (stop-router!)
  (stop-web-server!))

(defn start! []
  (start-router!)
  (start-web-server!))

(defn -main []
  (start!))