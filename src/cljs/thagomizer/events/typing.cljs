(ns thagomizer.events.typing
  (:require
   [re-frame.core :as rf]))

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
