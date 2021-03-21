(ns thagomizer.chat.ws.events
  (:require
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre :refer-macros [infof  warnf]]
   [thagomizer.chat.ws.utils :as ws-utils]
   [thagomizer.chat.events.typing :as typing-events]
   [thagomizer.chat.events.messages :as message-events]
   [thagomizer.chat.events.uids :as uid-events]
   [thagomizer.chat.events.visibility :as visibility-events]))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as _ev-msg :keys [event]}]
  (warnf "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/handshake
  [{:as _ev-msg :keys [?data]}]
  (infof "Handshake: %s" ?data))

(defmethod -event-msg-handler :chsk/recv
  [{:as _ev-msg :keys [?data]}]
  (let [[event-id {:keys [msg]}] ?data]
    (cond
      (= event-id :thagomizer/message)
      (do
        (rf/dispatch [::message-events/set-latest-message (second ?data)])
        (rf/dispatch [::visibility-events/play-sound]))
      (= event-id :thagomizer/typing-status)
      (rf/dispatch [::typing-events/set-typing-status msg])
      (= event-id :thagomizer/new-user)
      (rf/dispatch [::uid-events/add-uid msg])
      (= event-id :thagomizer/login)
      (do
        (rf/dispatch [::uid-events/set-self-uid msg])
        (rf/dispatch [::uid-events/set-uids msg]))
      (= event-id :thagomizer/lost-user)
      (do
        (rf/dispatch [::message-events/handle-inactive-user-messages msg])
        (rf/dispatch [::uid-events/remove-uid msg]))
      :else (ws-utils/->output! (str "Shit went sideways" event-id)))))

(defmethod -event-msg-handler :thagomizer/message
  [{:as _ev-msg :keys [?data]}]
  (ws-utils/->output! "Message: %s" ?data))
