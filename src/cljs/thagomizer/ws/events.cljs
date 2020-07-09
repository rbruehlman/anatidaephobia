(ns thagomizer.ws.events
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as encore :refer-macros (have)]
   [taoensso.timbre :as timbre :refer-macros [infof  warnf]]
   [thagomizer.ws.utils :as ws-utils]
   [thagomizer.events.typing :as typing-events]
   [thagomizer.events.messages :as message-events]))

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

(defmethod -event-msg-handler :chsk/state
  [{:as _ev-msg :keys [?data]}]
  (let [[_old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (infof "Channel socket successfully established!: %s" new-state-map)
      (infof "Channel socket state change: %s"              new-state-map))))

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
    :else (ws-utils/->output! "done fucked up"))
  ))

(defmethod -event-msg-handler :thagomizer/message
  [{:as _ev-msg :keys [?data]}]
  (ws-utils/->output! "Message: %s" ?data))
