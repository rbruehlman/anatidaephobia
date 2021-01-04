(ns thagomizer.chat.components.camera.modal
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [thagomizer.chat.events.camera.modal :as modal-events]
   [thagomizer.chat.subs.camera.modal :as modal-subs]
   [thagomizer.chat.subs.camera.photo :as photo-subs]
   [thagomizer.chat.components.camera.stream :refer [streaming-section]]
   [thagomizer.chat.components.camera.photo :refer [photo-section]]))

(defn handle-click [this event]
  (let [target (.-target event)]
    (when (= target @this)
      (rf/dispatch [::modal-events/toggle-camera-modal false]))))

(defn camera-modal [state]
  (let [modal-status @(rf/subscribe [::modal-subs/camera-modal])
        visible-photo @(rf/subscribe [::photo-subs/photo-visibility])]
    [:div {:id "camera-modal"
           :class "modal"
           :style (merge
                   {:background-color "#D3D3D3"
                    :border-radius 15
                    :margin-top 30
                    :display (if modal-status :block :none)}
                   @state)}
     (if visible-photo
       [photo-section]
       [streaming-section state])]))

(defn modal-background [state]
  (let [this (r/atom nil)]
    (fn []
      [:div {:onClick #(handle-click this %)
             :ref #(reset! this %)
             :style {:background-color "rgba(255, 255, 255, .0)"
                     :width "100vw"
                     :height "100vh"
                     :left 0
                     :top 0
                     :position :absolute}}
       [camera-modal state]])))
