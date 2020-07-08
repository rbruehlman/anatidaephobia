(ns thagomizer.events.core
  (:require
   [clojure.string  :as str]
   [re-frame.core :as rf]
   [thagomizer.db :as db]
   [thagomizer.ws.client :as ws-client]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-fx
 ::update-text-field
 (fn [cofx [_ value]]
   (let [db  (:db cofx)
         was-typing? (get-in db [:typing-status :self])
         is-typing? (not (empty? value))]
     (when (not= was-typing? is-typing?)
       (ws-client/chsk-send! [:thagomizer/typing-status is-typing?] 5000))
     {:db (-> db
              (assoc :text-field value)
              (assoc-in [:is-typing :self] is-typing?))})))

(rf/reg-event-fx
 ::submit-message
 (fn [cofx]
   (let [db  (:db cofx)
         msg (:text-field db)
         is-typing? (:is-typing :self)]

     (if (str/blank? msg)
       (js/alert "Can't send a blank message!")
       (do
         (ws-client/chsk-send! [:thagomizer/typing-status is-typing?] 5000)
         (ws-client/chsk-send! [:thagomizer/message msg] 5000)
         (rf/dispatch [::update-text-field ""]))))))


(defn set-self-typing-status
  [db status]
  (assoc-in db [:is-typing :self] status))

(defn set-others-typing-status
  [db uid status]
  (let [method (if status (var conj) (var disj))]
    (update-in db [:is-typing :others] method uid)))

(rf/reg-event-db
 ::set-typing-status
 (fn [db [_ uid status]]
   (let [self (:uid db)]
     (if (= uid self)
       (set-self-typing-status db status)
       (set-others-typing-status db uid status)))))

