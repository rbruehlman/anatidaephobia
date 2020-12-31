(ns thagomizer.chat.components.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.camera :as camera-subs]
   [thagomizer.chat.events.camera :as camera-events]
   [thagomizer.common.components.input :refer [button]]))

(defn timer-circle [key]
  (let [timer @(rf/subscribe [::camera-subs/timer])]
  [:span
   {:style {:height "5px"
            :width "5px"
            :background-color (cond
                                (= timer 3.5) "red"
                                (>= timer key) "yellow"
                                :else "grey")
            :border-radius "50%"
            :display "inline-block"
            :margin "10px 3px"}
    :key (str key "-timer")}]))


(defn camera-stream [state]
  [:video
     {:auto-play true
      :style {:max-width (:width @state)
              :max-height (:height @state)
              :width "80%"
              :height "80%"
              :position "relative"
              :padding-top 40
              :left "50%"
              :transform "translateX(-50%)"}
      :ref (fn [com] (rf/dispatch [::camera-events/camera-element com]) com)}])


(defn streaming-section [state]
  
  (rf/dispatch [::camera-events/set-stream])
(rf/dispatch [::camera-events/set-timer 0])
  ;; readd visibility, you moron
  [:<>
    [camera-stream state]
    [:div {:style {:display "flex"
                   :align-items "center"
                   :justify-content "center"}}
     [timer-circle 1]
     [timer-circle 2]
     [timer-circle 3]]

    [:div {:style {:width "100%"
                   :display "flex"
                   :align-items "center"
                   :justify-content "center"}}
     [button [::camera-events/begin-countdown] "take-photo" "snap!"]]])


(defn photo-section [state photo]
  [:<>
   [:img ]
   ]
)

(defn camera-modal
  "Modal for camera stream and photo"
  [state]
  (let [modal-status @(rf/subscribe [::camera-subs/camera-modal])
        camera-stream (rf/subscribe [::camera-subs/stream])
        photo (rf/subscribe [::camera-subs/photo])]

    [:div {:id "camera-modal"
           :class "modal"
           :style (merge
                   {:background-color "grey"
                    :border-radius "15px"
                    :display (if modal-status "block" "none")}
                   @state)
           :key "camera-modal"}
     [:div [:div {:class "close"
                  :onClick #(rf/dispatch [::camera-events/stop-media-stream
                                          ::camera-events/toggle-camera-modal false])
                  :key "close-x"
                  :dangerouslySetInnerHTML {:__html "&times;"}
                  :style {:float "right"
                          :margin-right "35px"}}]]
     (cond
       (and (nil? @camera-stream) (nil? @photo))
       [streaming-section state]
       :else [streaming-section state]
       )]))
    
     

     

     
