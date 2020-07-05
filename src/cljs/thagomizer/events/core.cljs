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
    (let [db  (:db cofx)]
     (ws-client/chsk-send! [:thagomizer/publish "is typing..."] 5000)
     {:db (assoc db :text-field value)})))

(rf/reg-event-db
 ::submit-message
 (fn [db [_ value]]
   (let [msg (:text-field db)]
     (if (str/blank? msg)
       (js/alert "Can't send a blank message!")
       (ws-client/chsk-send! [:thagomizer/publish value] 5000)))))