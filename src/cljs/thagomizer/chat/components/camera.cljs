(ns thagomizer.chat.components.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.camera :as camera-subs]
   [thagomizer.chat.events.camera :as camera-events]
   [thagomizer.common.components.input :refer [button]]))

(defn camera-stream [state]
  (let [dimensions (into {} (for [[k v] state] [k (- v 80)]))]
    [:video
     {:auto-play true
      :style dimensions
      :ref (fn [com] (rf/dispatch [::camera-events/camera-element com]) com)}]))

(defn camera-modal
  "Modal for camera stream and photo"
  [state]
  (let [modal-status @(rf/subscribe [::camera-subs/camera-modal])]

    [:div {:id "camera-modal"
           :class "modal"
           :style (merge
                   {:background-color "grey"
                    :border-radius "15px"
                    :display (if modal-status "block" "none")}
                   @state
                   )
           :key "camera-modal"}
     [:div {:class "modal-content" :key "modal-content"}
      [:div [:div {:class "close"
                   :onClick #(rf/dispatch [::camera-events/stop-media-stream
                                           ::camera-events/toggle-camera-modal false])
                   :key "close-x"
                   :dangerouslySetInnerHTML {:__html "&times;"}
                   :style {:float "right"
                           :margin-right "35px"}}]]

      [camera-stream @state]
      (rf/dispatch [::camera-events/get-media-stream])
      [button [::camera-events/update-active-photo] "take-photo" "snap!"]]]))





