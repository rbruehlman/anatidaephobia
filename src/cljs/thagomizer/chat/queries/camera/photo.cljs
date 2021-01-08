(ns thagomizer.chat.queries.camera.photo)

(defn set-photo-visibility [db bool]
  (assoc-in db [:chat :camera :photo :visible] bool))

(defn get-photo-visibility [db]
  (get-in db [:chat :camera :photo :visible]))

(defn set-photo-data [db url]
  (assoc-in db [:chat :camera :photo :url] url))

(defn get-photo-data [db]
  (get-in db [:chat :camera :photo :url]))

(defn set-photo-error [db error]
  (assoc-in db [:chat :camera :photo :error] error))

(defn get-photo-error [db]
  (get-in db [:chat :camera :photo :error]))

(defn set-photo-loading-status [db status]
  (assoc-in db [:chat :camera :photo :loading] status))

(defn get-photo-loading-status [db]
  (get-in db [:chat :camera :photo :loading]))

(defn clear-photo [db]
  (-> db
      (set-photo-visibility false)
      (set-photo-loading-status nil)
      (set-photo-error nil)
      (set-photo-data nil)))