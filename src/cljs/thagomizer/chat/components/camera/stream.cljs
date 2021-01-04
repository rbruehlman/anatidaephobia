(ns thagomizer.chat.components.camera.stream
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [thagomizer.chat.subs.camera.stream :as stream-subs]
   [thagomizer.chat.events.camera.stream :as stream-events]
   [thagomizer.chat.subs.camera.photo :as photo-subs]
   [thagomizer.common.components.input :refer [button]]))

(defn timer-circle [timer key]
    [:span
     {:style {:height "5px"
              :width "5px"
              :background-color (cond
                                  (= timer 4.5) "red"
                                  (>= timer key) "yellow")
              :hidden (= timer 0)
              :border-radius "50%"
              :display "inline-block"
              :margin "10px 3px"}
      :key (str key "-timer")}])


(defn camera-stream [state]

  (r/create-class
   {:component-did-mount
    (fn []
      (rf/dispatch [::stream-events/set-stream-element])
      (rf/dispatch [::stream-events/set-timer 0]))

    :component-will-unmount
    (fn []
      (rf/dispatch [::stream-events/stop-stream]))

    :reagent-render
    (fn []
      [:div {:style {:overflow "hidden"
                     :max-width (:width @state)
                     :max-height (:height @state)
                     :width "100%"}}
       [:video
        {:auto-play true
         :on-play #(rf/dispatch [::stream-events/set-stream-loading-status :loaded])
         :style {:max-width (:width @state)
                 :max-height (:height @state)
                 :width "80%"
                 :height "80%"
                 :position "relative"
                 :left "50%"
                 :top "10%"
                 :transform "translate(-50%, 10%)"
                 :object-position "center top"}
         :ref (fn [com] (rf/dispatch [::stream-events/camera-element com]) com)}]])}))


(defn streaming-section [state]
  (let [stream-loading-status     @(rf/subscribe [::stream-subs/stream-loading-status])
        photo-loading-status      @(rf/subscribe [::photo-subs/photo-loading-status])
        timer                     @(rf/subscribe [::stream-subs/timer])]

    [:<>
     [camera-stream state]

     (when (= :loaded stream-loading-status)
       [:<>
        [:div {:style {:display "flex"
                       :align-items "center"
                       :justify-content "center"}}
         [timer-circle timer 1]
         [timer-circle timer 2]
         [timer-circle timer 3]
         [timer-circle timer 4]]
        
        (when (nil? photo-loading-status)
          [:div {:style {:width "100%"
                         :display "flex"
                         :align-items "center"
                         :justify-content "center"}}
         [button [::stream-events/begin-countdown] "take-photo" "snap!"]])])]))