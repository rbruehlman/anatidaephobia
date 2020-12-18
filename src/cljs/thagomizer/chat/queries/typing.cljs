(ns thagomizer.chat.queries.typing)

(defn set-self-typing-status [db bool]
  (assoc-in db [:chat :typing-status :self] bool))

(defn set-others-typing-status
  [db uid status]
  (let [method (if status (var conj) (var disj))]
    (update-in db [:chat :is-typing :others] method uid)))

(defn get-self-typing-status [db]
  (get-in db [:chat :typing-status :self]))

(defn get-others-typing-status [db]
  (get-in db [:chat :is-typing :others]))


