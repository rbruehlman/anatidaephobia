(ns thagomizer.chat.events.messages
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.messages :as message-q]
   [thagomizer.entry.queries.authentication :as auth-q]))

;; Add the latest message to the DB message FIFO queue (limited at 10 items)
(rf/reg-event-fx
 ::set-latest-message
 (fn [cofx [_ event-data]]
   (let [db (:db cofx)
         message-queue (message-q/get-messages db)
         new-message (:msg event-data)]
     {:db (if (> (count message-queue) 10)
            (message-q/replace-message db new-message)
            (message-q/add-message db new-message))})))


;;scroll down to the end of the messages
(rf/reg-event-fx
 ::scroll-down
 (fn [cofx]
   (let [message-list (.getElementById js/document "message-list")]
     (.scrollTo message-list 0 (.-scrollHeight message-list)))))

;;remove messages from inactive users, if not admin
(rf/reg-event-db
 ::handle-inactive-user-messages
 (fn [db [_ msg]]
   (let [lost-uid (keyword msg)
         messages (message-q/get-messages db)
         admin (auth-q/get-admin-status db)]
      (if admin
        (message-q/update-former-uid-msgs db lost-uid messages)
        (message-q/remove-former-uid-msgs db lost-uid messages)))))