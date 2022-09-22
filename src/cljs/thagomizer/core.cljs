(ns thagomizer.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as rf]
   [thagomizer.common.events.etc :as events]
   [thagomizer.chat.ws.client :as client]
   [thagomizer.config :as config]
   [thagomizer.chat.ws.events :as ws-events]
   [thagomizer.entry.components.core :as components]
   [taoensso.sente  :as sente]
   [day8.re-frame.http-fx]))


(defonce router_ (atom nil))

(defn  stop-router! []
  (when-let [stop-f @router_] (stop-f)))

(defn start-router! []
    (stop-router!)
    (reset! router_
            (sente/start-client-chsk-router!
             client/ch-chsk ws-events/event-msg-handler)))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (rdom/render [components/app]
               (.getElementById js/document "app")))

(defn dev-setup
  "Sets environment up for development."
  []
  (when config/debug?
    (enable-console-print!)
    (println "👩‍💻 You're in dev mode now")))

(defn ^:export main
  "Main app init function."
  []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (start-router!))