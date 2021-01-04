(ns thagomizer.chat.queries.camera.modal
  (:require [thagomizer.chat.queries.camera.photo :as photo-q]
            [thagomizer.chat.queries.camera.stream :as stream-q]))

(defn set-camera-modal [db bool]
  (assoc-in db [:chat :camera :modal] bool))

(defn get-camera-modal [db]
  (get-in db [:chat :camera :modal]))

(defn clear-modal [db]
  (-> db
      (photo-q/clear-photo)
      (stream-q/clear-stream)
      (set-camera-modal false)))
