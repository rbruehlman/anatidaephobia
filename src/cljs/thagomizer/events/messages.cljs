(ns thagomizer.events.messages
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]))

;;Goodbye message!  We are popping you from the FIFO queue
(rf/reg-event-db
 ::remove-message
 (fn [db]
   (update db :messages pop)))

;; Add the latest message to the DB message FIFO queue (limited at 10 items)
;; and set a timeout effect to remove the message after 5 minutes.
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
                    conj new-message))
      :timeout {:id (:timestamp event-data)
                :event [::remove-message]
                :time 300000 ;; 5 minutes
                }})))

(defonce timeouts (reagent/atom {}))

;;Fire an event after a certain amount of time!
(rf/reg-fx
 :timeout
 (fn [{:keys [id event time]}]
   (when-some [existing (get @timeouts id)]
     (js/clearTimeout existing)
     (swap! timeouts dissoc id))
   (when (some? event)
     (swap! timeouts assoc id
            (js/setTimeout
             (fn []
               (rf/dispatch event))
             time)))))

;;scroll down to the end of the messages :) :) :)
(rf/reg-event-fx
 ::scroll-down
 (fn [cofx]
   (let [message-list (.getElementById js/document "message-list")]
     (.scrollTo message-list 0 (.-scrollHeight message-list)))))