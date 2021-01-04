(ns thagomizer.chat.queries.camera.stream)

(defn set-stream-visibility [db bool]
  (assoc-in db [:chat :camera :stream :visible] bool))

(defn get-stream-visibility [db]
  (get-in db [:chat :camera :stream :visible]))

(defn set-stream-element [db elem]
  (assoc-in db [:chat :camera :stream :element] elem))

(defn get-stream-element [db]
  (get-in db [:chat :camera :stream :element]))

(defn set-stream-object [db obj]
  (assoc-in db [:chat :camera :stream :object] obj))

(defn get-stream-object [db]
  (get-in db [:chat :camera :stream :object]))

(defn set-stream-error [db error]
  (assoc-in db [:chat :camera :stream :error] error))

(defn get-stream-error [db]
  (get-in db [:chat :camera :stream :error]))

(defn set-stream-loading-status [db status]
  (assoc-in db [:chat :camera :stream :loading] status))

(defn get-stream-loading-status [db]
  (get-in db [:chat :camera :stream :loading]))

;; timer

(defn set-timer [db val]
  (assoc-in db [:chat :camera :stream :timer] val))

(defn get-timer [db]
  (get-in db [:chat :camera :stream :timer]))


(defn clear-stream [db]
  (-> db
      (set-stream-object nil)
      (set-stream-element nil)
      (set-stream-loading-status nil)
      (set-stream-visibility false)
      (set-timer 0)))