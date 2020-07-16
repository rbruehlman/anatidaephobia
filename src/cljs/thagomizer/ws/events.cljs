(ns thagomizer.ws.events
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as encore :refer-macros (have)]
   [taoensso.timbre :as timbre :refer-macros [infof  warnf]]
   [thagomizer.ws.utils :as ws-utils]
   [thagomizer.events.typing :as typing-events]
   [thagomizer.events.messages :as message-events]
   [thagomizer.events.uids :as uid-events]))

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
  (let [[event-id {:keys [uid msg]}] ?data]
    (cond
    (= event-id :thagomizer/message)
      (rf/dispatch [::message-events/set-latest-message (second ?data)])
    (= event-id :thagomizer/typing-status)
      (rf/dispatch [::typing-events/set-typing-status uid msg])
    (= event-id :thagomizer/connected-uids)
      (rf/dispatch [::uid-events/set-uids msg])
    :else (ws-utils/->output! (str "Shit went sideways" event-id)))
  ))

(defmethod -event-msg-handler :thagomizer/message
  [{:as _ev-msg :keys [?data]}]
  (ws-utils/->output! "Message: %s" ?data))
