(ns thagomizer.chat.components.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.camera :as camera-subs]
   [thagomizer.chat.events.camera :as camera-events]))

(defn camera-stream []
  [:video
   {:auto-play true
    :ref (fn [com] (rf/dispatch [::camera-events/camera-element com]) com)}])


(defn capture-button
  []
  [:button {:style {:font-size 11
                    :font-family "Gloria Hallelujah, cursive"
                    :border "none"
                    :text-align "center"
                    :border-radius "15px"
                    :padding "5px 8px 5px 8px"
                    :margin "5px"
                    :position "absolute"
                    :bottom 0}
            :type "submit"
            :value "snap!"
            :onClick #(rf/dispatch [::camera-events/update-active-photo])
            :key "take-photo"}
   "snap!"])

(defn camera-modal
  "Modal for camera stream and photo"
  [state]
  (let [modal-status @(rf/subscribe [::camera-subs/camera-modal])
        streaming @(rf/subscribe [::camera-subs/stream])]
    
  [:div {:id "camera-modal"
         :class "modal"
         :style {:min-width 382
                 :max-width 382
                 :min-height 382
                 :max-height 382
                 :width (:width @state)
                 :top (:top @state)
                 :left (:left @state)
                 :height (:height @state)
                 :background-color "grey"
                 :border-radius "15px"
                 :display (if modal-status "block" "none")}
         :key "camera-modal"}
   [:div {:class "modal-content" :key "modal-content"}
    [:div [:div {:class "close"
                 :onClick #(rf/dispatch [::camera-events/stop-media-stream
                                         ::camera-events/toggle-camera-modal false])
                 :key "close-x"
                 :dangerouslySetInnerHTML {:__html "&times;"}
                 :style {:float "right"
                         :margin-right "35px"}}]]

    [camera-stream]
    (rf/dispatch [::camera-events/get-media-stream])
    [capture-button]]]))




