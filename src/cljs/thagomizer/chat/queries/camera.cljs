(ns thagomizer.chat.queries.camera)

(defn set-stream [db stream]
  (assoc-in db [:chat :camera :stream :visible] stream))

(defn set-photo [db url]
  (assoc-in db [:chat :camera :photo :url] url))

(defn set-element [db elem]
  (assoc-in db [:chat :camera :stream :element] elem))

(defn set-camera-modal [db bool]
  (assoc-in db [:chat :camera :modal] bool))

(defn set-stream-error [db error]
  (assoc-in db [:chat :camera :stream :error] error))

(defn set-photo-error [db error]
  (assoc-in db [:chat :camera :stream :error] error))

(defn set-timer [db val]
  (assoc-in db [:chat :camera :photo :timer] val))


;;get

(defn get-stream [db]
  (get-in db [:chat :camera :stream :visible]))

(defn get-photo [db]
  (get-in db [:chat :camera :photo :visible]))

(defn get-element [db]
  (get-in db [:chat :camera :stream :element]))

(defn get-camera-modal [db]
  (get-in db [:chat :camera :modal]))


(defn get-stream-error [db]
  (get-in db [:chat :camera :stream :error]))

(defn get-photo-error [db]
  (get-in db [:chat :camera :photo :error]))

(defn get-timer [db]
  (get-in db [:chat :camera :photo :timer]))