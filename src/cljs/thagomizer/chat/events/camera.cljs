(ns thagomizer.chat.events.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera :as camera-q]))

(rf/reg-event-db
 ::toggle-camera-modal
 (fn [db [_ bool]]
   (camera-q/set-camera-modal db bool)))

(defn handle-video-success
  [db stream]
  (set! (.-srcObject (camera-q/get-element db)) stream)
  (.play (.-srcObject (camera-q/get-element db)))
  db
  )


(rf/reg-event-fx
 ::get-media-stream
 (fn [cofx]
   (let [db  (:db cofx)]
     {:db (camera-q/set-stream db
                    (-> js/navigator
                        .-mediaDevices
                        (.getUserMedia #js {:video true :audio false})
                        (.then (partial handle-video-success db))
                        (.catch #(camera-q/set-stream-error db %))))})))

(rf/reg-event-fx
 ::stop-media-stream
 (fn [cofx]
   (let [db  (:db cofx)
         stream (:stream db)]
   (doseq [t (.getTracks stream)]
      (.stop t))
     (assoc-in db [:chat :camera :stream :object] nil))))

(rf/reg-event-db
 ::stream-loading-status
 (fn [db [_ bool]]
   (camera-q/set-stream-loading-status db bool)))

(rf/reg-event-db
 ::update-active-photo
 (fn [db [_ url]]
   (camera-q/set-active-photo db url)))

(rf/reg-event-fx
 ::camera-stream-and-modal
 (fn [cofx]
   {:db (:db cofx)
    :fx [[:dispatch [::toggle-camera-modal true]]]}))

(rf/reg-event-db
 ::camera-element
 (fn [db [_ com]]
   (camera-q/set-element db com)))


