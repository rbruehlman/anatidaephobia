(ns thagomizer.chat.events.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera :as camera-q]
   [thagomizer.chat.events.utils :as utils]))

(rf/reg-event-db
 ::toggle-camera-modal
 (fn [db [_ bool]]
   (camera-q/set-camera-modal db bool)))

(defn handle-video-success
  [db stream]
  (set! (.-srcObject (camera-q/get-element db)) stream)
  (.play (.-srcObject (camera-q/get-element db)))
  db)

(rf/reg-event-fx
 ::set-stream
 (fn [cofx]
   (let [db  (:db cofx)]
     {:db (camera-q/set-stream db
                               (-> js/navigator
                                   .-mediaDevices
                                   (.getUserMedia #js {:video true :audio false})
                                   (.then (partial handle-video-success db))
                                   (.catch #(camera-q/set-stream-error db %))))})))

(rf/reg-event-fx
 ::stop-stream
 (fn [cofx]
   (let [db  (:db cofx)
         stream (:stream db)]
     (doseq [t (.getTracks stream)]
       (.stop t))
     (camera-q/set-stream db nil))))

(rf/reg-event-db
 ::update-photo
 (fn [cofx]
   (let [db  (:db cofx)
         stream (camera-q/get-stream db)]
     (->
      ((.-takePhoto stream))
      (.then
       (fn [blob]
         (.createObjectURL js/URL blob)))
      (.catch (fn [error] (camera-q/set-photo-error db error)))))))

(rf/reg-event-fx
 ::camera-stream-and-modal
 (fn [cofx]
   {:db (:db cofx)
    :fx [[:dispatch [::toggle-camera-modal true]]]}))

(rf/reg-event-db
 ::camera-element
 (fn [db [_ com]]
   (println com)
   (camera-q/set-element db com)))

(rf/reg-event-db
 ::set-timer
 (fn [db [_ val]]
   (camera-q/set-timer db val)))

(defn countdown [e]
  (into [] (map #(assoc {:event e}
                        :timeout (* % 1000))
                [1 2 3 3.5])))

(rf/reg-event-fx
 ::begin-countdown
 (fn []
   {:timeout-fx (concat (countdown ::set-timer)
                        [{:event [::update-active-photo]
                          :time 3500}
                         {:event [::set-timer 0]
                          :time 4300}])}))
