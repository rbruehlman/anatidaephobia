(ns thagomizer.events.input
  (:require
   [clojure.string  :as str]
   [re-frame.core :as rf]
   [thagomizer.ws.client :as ws-client]))

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