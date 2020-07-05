(ns thagomizer.handler
  (:require
   [ring.middleware.anti-forgery :as anti-forgery]
   [ring.middleware.defaults]
   [taoensso.timbre :as timbre :refer (debugf)]
   [hiccup.core        :as hiccup]
   [thagomizer.server :as server]
   [thagomizer.utils :as utils]))


(defonce router_ (atom nil))

(defn landing-pg-handler [ring-req]
  (hiccup/html
   [:h1 "Sente reference example"]
   (let [csrf-token
         (force anti-forgery/*anti-forgery-token*)]
     [:div#sente-csrf-token {:data-csrf-token csrf-token}])
   [:p [:strong "Step 2: "] " observe std-out (for server output) and below (for client output):"]
   [:textarea#output {:style "width: 100%; height: 200px;"}]
   [:div#app]
   [:script {:src "main.js"}] ; Include our cljs target
   ))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

; Default/fallback case (no other matching handler)
(defmethod -event-msg-handler
  :default
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid (:uid session)]
    (debugf "Unhandled event: %s" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-from-server event}))))

(defn publish [event-msg]
  (doseq [uid (:any @server/connected-uids)]
    (let [to-publish {:uid uid
                      :timestamp utils/now-time
                      :data (:?data event-msg)}]
    (server/chsk-send! uid [:thagomizer/publish to-publish]))))

(defmethod -event-msg-handler :thagomizer/publish
  [ev-msg] (publish ev-msg))

