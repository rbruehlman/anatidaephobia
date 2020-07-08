(ns thagomizer.handler
  (:require
   [ring.middleware.anti-forgery :as anti-forgery]
   [ring.middleware.defaults]
   [taoensso.timbre :as timbre :refer (debugf)]
   [hiccup.page :refer [html5]]
   [thagomizer.ws :as ws]
   [thagomizer.utils :as utils]))

(defn landing-pg-handler [ring-req]
  (html5 {:ng-app "Thagomizer" :lang "en"}
         [:head
          [:title "The Thagomizer"]
          [:link {:href "https://fonts.googleapis.com/css2?family=Gloria+Hallelujah&display=swap"
                  :rel "stylesheet"}]]
         [:body
          [:div {:class "container"}
           (let [csrf-token
                 (force anti-forgery/*anti-forgery-token*)]
             [:div#sente-csrf-token {:data-csrf-token csrf-token}])
           [:div#app]
           [:script {:src "main.js"}]]] ; Include our cljs target
         ))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg}]
  (-event-msg-handler ev-msg))

; Default/fallback case (no other matching handler)
(defmethod -event-msg-handler
  :default
  [{:as _ev-msg :keys [event ring-req ?reply-fn]}]
  (let [session (:session ring-req)] ;; null pointer exception if not left in? ¯\_(ツ)_/¯
    (debugf "Unhandled event: %s" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-from-server event}))))

(defn- publish [event data uids]
  (doseq [uid uids]
    (let [to-publish {:uid uid
                      :timestamp (utils/now-time)
                      :msg data}]
      (ws/chsk-send! uid [event to-publish]))))

(defn publish-to-all [event event-msg]
  (publish event event-msg (:any @ws/connected-uids)))

(defn publish-to-others [event data ring-req]
  (let [all           (:any           @ws/connected-uids)
        params        (:params        ring-req)
        client-id     (:client-id     params)
        others        (filter #(not= % client-id) all)]

    (publish event data others)))

(defmethod -event-msg-handler
  :thagomizer/typing-status
  [{:as _ev-msg :keys [ring-req ?data]}]
  (publish-to-others :thagomizer/typing-status ?data ring-req))

(defmethod -event-msg-handler
  :thagomizer/message
  [{:as _ev-msg :keys [?data]}]
  (publish-to-all :thagomizer/message ?data))

