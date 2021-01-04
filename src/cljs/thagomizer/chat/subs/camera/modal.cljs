(ns thagomizer.chat.subs.camera.modal
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.modal :as modal-q]))

(rf/reg-sub
 ::camera-modal
 (fn [db]
   (modal-q/get-camera-modal db)))
