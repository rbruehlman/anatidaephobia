(ns thagomizer.chat.events.typing
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.typing :as typing-q]
   [thagomizer.chat.queries.uids :as uid-q]))

;;Sets typing status based on who is typing (you or someone else)
(rf/reg-event-db
 ::set-typing-status
 (fn [db [_ msg]]
   (let [self (uid-q/get-self-uid db)
         typer (first (keys msg))
         status (typer msg)]
     (if (= typer self)
       (typing-q/set-self-typing-status db status)
       (typing-q/set-others-typing-status db typer status)))))

;;We should use this when someone leaves
(rf/reg-event-db
 ::clear-typing-status
 (fn [db [_ msg]]
   (let [lost-uid (keyword msg)]
     (typing-q/set-others-typing-status db lost-uid false))))

