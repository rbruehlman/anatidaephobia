(ns thagomizer.receipt.queries)

(defn set-messages [db messages]
  (assoc-in db [:receipt :messages :data] (sort-by :id messages)))

(defn set-message-error [db error]
  (assoc-in db [:receipt :messages :error] error))

(defn get-messages [db]
  (get-in db [:receipt :messages :data]))

(defn get-message-error [db]
  (get-in db [:receipt :messages :error]))