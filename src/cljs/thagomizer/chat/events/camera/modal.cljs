(ns thagomizer.chat.events.camera.modal
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.modal :as modal-q]))

(rf/reg-event-db
 ::toggle-camera-modal
 (fn [db [_ bool]]
   (if bool
     (modal-q/set-camera-modal db bool)
     (modal-q/clear-modal db))))
