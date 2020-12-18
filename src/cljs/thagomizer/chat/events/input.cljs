(ns thagomizer.chat.events.input
  (:require
   [clojure.string  :as str]
   [re-frame.core :as rf]
   [thagomizer.chat.ws.client :as ws-client]
   [thagomizer.chat.queries.uids :as uid-q]
   [thagomizer.chat.queries.input :as input-q]
   [thagomizer.chat.queries.typing :as typing-q]
   ))

;; Update the text field value with the characters submitted.
;; If the text field value is empty, set is-typing to False.
;; If the text field value isn't empty, then the person is 
;; considered to be typing (even if they've paused)"
(rf/reg-event-fx
 ::update-text-field
 (fn [cofx [_ value]]
   (let [db  (:db cofx)
         was-typing? (typing-q/get-self-typing-status db)
         is-typing? (not (empty? value))
         uid (uid-q/get-self-uid db)]
     (when (not= was-typing? is-typing?)
       (ws-client/chsk-send! [:thagomizer/typing-status {(keyword uid) is-typing?}] 500))
     {:db (-> db
              (input-q/set-text-field value)
              (typing-q/set-self-typing-status is-typing?))})))

;;Send a message and then clear the text field
(rf/reg-event-fx
 ::submit-message
 (fn [cofx]
   (let [db  (:db cofx)
         msg (input-q/get-text-field db)
         self (uid-q/get-self-uid db)
         is-typing? (typing-q/get-self-typing-status db)]

     (if (str/blank? msg)
       (js/alert "Can't send a blank message!")
       (do
         (ws-client/chsk-send! [:thagomizer/typing-status {(keyword self) is-typing?}] 500)
         (ws-client/chsk-send! [:thagomizer/message  msg] 500)
         (rf/dispatch [::update-text-field ""]))))))