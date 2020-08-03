(ns thagomizer.events.typing
  (:require
   [re-frame.core :as rf]))

;;Assigns a bool if you are typing
(defn set-self-typing-status
  [db status]
  (assoc-in db [:is-typing :self] status))

;;Sets a uid/bool map for others who are typing (or not)
(defn set-others-typing-status
  [db uid status]
  (let [method (if status (var conj) (var disj))]
    (update-in db [:is-typing :others] method uid)))

;;Sets typing status based on who is typing (you or someone else)
(rf/reg-event-db
 ::set-typing-status
 (fn [db [_ uid status]]
   (let [self (:uid db)]
     (if (= uid self)
       (set-self-typing-status db status)
       (set-others-typing-status db uid status)))))
