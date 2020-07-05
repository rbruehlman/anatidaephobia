(ns thagomizer.core
  (:require
   [taoensso.sente :as sente]
   [ring.middleware.defaults]
   [org.httpkit.server :as http-kit]
   [thagomizer.routes :as routes]
   [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]))

(let [packer :edn
      chsk-server
      (sente/make-channel-socket-server!
       (get-sch-adapter) {:packer packer})

      {:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      chsk-server]

  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

;; We can watch this atom for changes if we like
(add-watch connected-uids :connected-uids
           (fn [_ _ old new]
             (when (not= old new)
               (infof "Connected uids change: %s" new))))

(defonce router_ (atom nil))

(def main-ring-handler
  (ring.middleware.defaults/wrap-defaults
   routes/ring-routes ring.middleware.defaults/site-defaults))

(defn stop-router! []
  (when-let [stop-fn router_]
    (stop-fn)))

(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router!
           server/ch-chsk handler/event-msg-handler)))

(defonce web-server_ (atom nil))
(defn stop-web-server! [] (when-let [stop-fn @web-server_] (stop-fn)))

(defn start-web-server! [& [port]]
  (stop-web-server!)
  (let [port (or port 0)                                ; 0 => Choose any available port	
        ring-handler (var router/main-ring-handler)
        [port stop-fn]
        (let [stop-fn (http-kit/run-server ring-handler {:port port})]
          [(:local-port (meta stop-fn)) (fn [] (stop-fn :timeout 100))])
        uri (format "http://localhost:%s/" port)]

    (try
      (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
      (catch java.awt.HeadlessException _))

    (reset! web-server_ stop-fn)))

(defn stop! []
  (router/stop-router!)
  (server/stop-web-server!))

(defn start! []
  (router/start-router!)
  (server/start-web-server!))

(defn -main []
  (start!))