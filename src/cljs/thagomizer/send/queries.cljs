(ns thagomizer.send.queries)

(defn set-text-field [db text]
  (assoc-in db [:send :text-field] text))

(defn get-text-field [db]
  (get-in db [:send :text-field]))
