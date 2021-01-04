(ns thagomizer.chat.events.camera.stream
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.stream :as stream-q]
   [thagomizer.chat.events.camera.photo :as photo-events]
   [thagomizer.chat.events.utils :as utils]))


(defn set-stream-to-element-src-object
  [db stream]
  (set! (.-srcObject (stream-q/get-stream-element db)) stream))

(rf/reg-event-db
 ::set-stream-object
 (fn [db [_ stream]]
   (stream-q/set-stream-object db stream)))

(rf/reg-event-fx
 ::set-stream-element
 (fn [cofx]
   (let [db  (:db cofx)]
     (-> js/navigator
         .-mediaDevices
         (.getUserMedia #js {:video true :audio false})
         (.then (partial set-stream-to-element-src-object db))
         (.then #(rf/dispatch [::set-stream-object %]))
         (.catch #(stream-q/set-stream-error db %)))
     {:dispatch [::set-stream-visibility true]})))

(rf/reg-event-fx
 ::stop-stream
 (fn [cofx]
   (let [db  (:db cofx)]
     (when-let [stream (stream-q/get-stream-object db)]
             (doseq [t (.getTracks stream)]
               (.stop t)))

     {:db (stream-q/clear-stream db)})))

(rf/reg-event-db
 ::camera-element
 (fn [db [_ com]]
   (stream-q/set-stream-element db com)))

(rf/reg-event-db
 ::set-stream-loading-status
 (fn [db [_ bool]]
   (stream-q/set-stream-loading-status db bool)))

(rf/reg-event-db
 ::set-stream-visibility
 (fn [db [_ bool]]
   (stream-q/set-stream-visibility db bool)))

;; timer

(rf/reg-event-db
 ::set-timer
 (fn [db [_ val]]
   (stream-q/set-timer db val)))

(defn countdown [e]
  (into [] (map #(assoc {:event [e %]}
                        :timeout (* % 1000))
                [1 2 3 4 4.5])))

(rf/reg-event-fx
 ::begin-countdown
 (fn []
   {::utils/timeout-fx (concat (countdown ::set-timer)
                               [{:event [::photo-events/take-photo]
                                 :timeout 4000}
                                {:event [::set-timer 0]
                                 :timeout 5000}])}))
