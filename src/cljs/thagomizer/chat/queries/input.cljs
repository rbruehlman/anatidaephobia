(ns thagomizer.chat.queries.input)

(defn set-text-field [db text]
  (assoc-in db [:chat :text-field] text))

(defn get-text-field [db]
  (get-in db [:chat :text-field]))
