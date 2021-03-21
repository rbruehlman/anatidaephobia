(ns thagomizer.chat.queries.visibility)

(defn set-hidden-value [db val]
  (assoc-in db [:chat :hidden] val))

(defn get-hidden-value [db]
  (get-in db [:chat :hidden]))