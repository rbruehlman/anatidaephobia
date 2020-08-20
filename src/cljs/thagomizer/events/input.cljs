(ns thagomizer.events.input
  (:require
   [clojure.string  :as str]
   [re-frame.core :as rf]
   [thagomizer.ws.client :as ws-client]))

;; Update the text field value with the characters submitted.
;; If the text field value is empty, set is-typing to False.
;; If the text field value isn't empty, then the person is 
;; considered to be typing (even if they've paused)"
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

;;Send a message and then clear the text field
(rf/reg-event-fx
 ::submit-message
 (fn [cofx]
   (let [db  (:db cofx)
         msg (:text-field db)
         is-typing? (:is-typing :self)]

     (if (str/blank? msg)
       (js/alert "Can't send a blank message!")
       (do
         (ws-client/chsk-send! [:thagomizer/typing-status is-typing?] 5000) ;; why do I need this again?
         (ws-client/chsk-send! [:thagomizer/message msg] 5000)
         (rf/dispatch [::update-text-field ""]))))))

(rf/reg-event-db
 ::update-passcode-field
  (fn [db [_ value]]
   (assoc db :passcode value)))

(rf/reg-event-db
 ::submit-passcode
 (fn [db]
   (assoc db :authenticated (= "m00m00" (:passcode db)))))