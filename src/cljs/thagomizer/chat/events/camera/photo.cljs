(ns thagomizer.chat.events.camera.photo
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.photo :as photo-q]
   [thagomizer.chat.queries.camera.stream :as stream-q]
   [thagomizer.chat.events.camera.modal :as modal-events]
   [thagomizer.chat.ws.client :as ws-client]))

(rf/reg-event-db
 ::set-photo-url
 (fn [db [_ url]]
   (photo-q/set-photo-url db url)))

(rf/reg-event-fx
 ::take-photo
 (fn [cofx]
   (let [db (:db cofx)
         stream (stream-q/get-stream-object db)
         image-capture (js/ImageCapture. (first (.getTracks stream)))]

     (rf/dispatch [::set-photo-loading-status :loading])

     (-> image-capture
         (.takePhoto)
         (.then #(.createObjectURL js/URL %))
         (.then #(rf/dispatch [::set-photo-url %]))
         (.catch #(.log js/console %)))

     {})))


(rf/reg-event-db
 ::set-photo-visibility
 (fn [db [_ bool]]
   (photo-q/set-photo-visibility db bool)))

(rf/reg-event-db
 ::set-photo-loading-status
 (fn [db [_ status]]
   (photo-q/set-photo-loading-status db status)))

(rf/reg-event-db
 ::clear-photo
 (fn [db]
   (photo-q/clear-photo db)))

(rf/reg-event-fx
 ::send-photo
 (fn [cofx]
   (let [db  (:db cofx)
         photo (photo-q/get-photo-url db)]
     (ws-client/chsk-send! [:thagomizer/message photo] 500))
   {:dispatch [::modal-events/toggle-camera-modal false]}))