(ns thagomizer.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as rf]
   [thagomizer.events.core :as events]
   [thagomizer.ws.client :as client]
   [thagomizer.config :as config]
   [thagomizer.ws.events :as ws-events]
   [thagomizer.components.core :as components]
   [thagomizer.components.utils :as c-utils]
   [stylefy.core :as stylefy]
   [taoensso.sente  :as sente]))


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
    (println "👩‍💻 You're in dev mode now")
    #_(println (c-utils/get-media-type))))

(defn ^:export main
  "Main app init function."
  []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (stylefy/init)
  (start-router!)
  (mount-root))
