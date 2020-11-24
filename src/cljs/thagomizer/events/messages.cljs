(ns thagomizer.events.messages
  (:require
   [clojure.set :as set]
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [thagomizer.utils :as utils]))

;; Add the latest message to the DB message FIFO queue (limited at 10 items)
(rf/reg-event-fx
 ::set-latest-message
 (fn [cofx [_ event-data]]
   (let [db (:db cofx)
         message-queue (:messages db)
         new-message (:msg event-data)]
     {:db (if (> (count message-queue) 10)
            (update db :messages
                    #(conj (pop %) new-message))
            (update db :messages
                    conj new-message))})))


;;scroll down to the end of the messages
(rf/reg-event-fx
 ::scroll-down
 (fn [cofx]
   (let [message-list (.getElementById js/document "message-list")]
     (.scrollTo message-list 0 (.-scrollHeight message-list)))))

(defn queue
  "Make things into a queue"
  ([coll]
   (reduce conj #queue [] coll)))

;;remove messages from inactive users, if history retention off
(rf/reg-event-db
 ::handle-inactive-user-messages
 (fn [db [_ msg]]
   (let [lost-uid (keyword (:lost-uid msg))
         messages (:messages db)
         history-retention (:history-retention db)]
     (if history-retention
       (update db :messages (queue (set/rename-keys messages {lost-uid :past-user})))
       (assoc db :messages (queue (filter #(not= lost-uid (keyword (:author %))) messages)))
       )
     )))

;;remove all messages
(rf/reg-event-db
 ::clear-messages
 (fn [db]
     (assoc db :messages #queue [])))