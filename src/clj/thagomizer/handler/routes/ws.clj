(ns thagomizer.handler.routes.ws
  (:require
   [clojure.string :as str]
   [ring.middleware.defaults]
   [taoensso.timbre :as timbre :refer (debugf)]
   [thagomizer.ws :as ws]))

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
  (when (not (nil? ?data)) ;;not sure why nil was being sent
    (publish-to-others :thagomizer/typing-status ?data ring-req)))

(defn- message-data
  [uid msg type]
  {:author uid
   :msg msg
   :type type
   :timestamp (.getTime (java.util.Date.))})

(defmethod -event-msg-handler
  :thagomizer/message
  [{:as _ev-msg :keys [?data uid]}]
  (let [msg ?data
        type "text"]
    (publish-to-all :thagomizer/message (message-data uid msg type))))

(defmethod -event-msg-handler
  :chsk/uidport-open
  [{:as _ev-msg :keys [uid ring-req]}]
  (publish :thagomizer/login
           {:self-uid uid :uids (:any @ws/connected-uids)} [uid])
  (publish-to-others :thagomizer/new-user uid ring-req)
  )

(defmethod -event-msg-handler
  :chsk/uidport-close
  [{:as _ev-msg :keys [uid ring-req]}]
  (let [msg (str (first (str/split (name uid) #"-")) " has left")
        type "exit"]
    (publish-to-others :thagomizer/lost-user uid ring-req)
    (publish-to-others :thagomizer/message (message-data uid msg type) ring-req)))
