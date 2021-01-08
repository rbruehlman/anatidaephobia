(ns thagomizer.chat.events.camera.photo
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.photo :as photo-q]
   [thagomizer.chat.queries.camera.stream :as stream-q]
   [thagomizer.chat.events.camera.modal :as modal-events]
   [thagomizer.chat.ws.client :as ws-client]))

(rf/reg-event-db
 ::set-photo-data
 (fn [db [_ data]]
   (photo-q/set-photo-data db data)))

(rf/reg-event-fx
 ::take-photo
 (fn [cofx]
   (let [db (:db cofx)
         stream (stream-q/get-stream-object db)
         image-capture (js/ImageCapture. (first (.getTracks stream)))]

     (rf/dispatch [::set-photo-loading-status :loading])

     (-> image-capture
         (.takePhoto)
         (.then #(rf/dispatch [::set-photo-data %]))
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
         photo (photo-q/get-photo-data db)
         reader (js/FileReader.)]
     
     (.readAsArrayBuffer reader photo)
     (set! (.-onload reader) #(ws-client/chsk-send! [:thagomizer/message {:msg (js->clj 
                                                                                (.from js/Array 
                                                                                       (js/Uint8Array.
                                                                                        (.-result reader))))
                                                                          :type "image"}] 500))
     
   {:dispatch [::modal-events/toggle-camera-modal false]})))