(ns thagomizer.events.messages
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]))

(rf/reg-event-db
 ::remove-message
 (fn [db]
   (update db :messages pop)))

(rf/reg-event-fx
 ::set-latest-message
 (fn [db [_ event-data]]
   (let [messages (:messages db)]
     {:db (if (> (count messages) 10)
            (update db :messages
                    #(conj (pop %) event-data))
            (update db :messages
                    conj event-data))
      :timeout {:id (:timestamp event-data)
                :event [::remove-message]
                :time 300000 ;; 5 minutes
                }})))

(defonce timeouts (reagent/atom {}))

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