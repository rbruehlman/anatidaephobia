(ns thagomizer.chat.components.camera.photo
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.camera.photo :as photo-subs]
   [thagomizer.chat.events.camera.photo :as photo-events]
   [thagomizer.common.components.input :refer [button]]))

(defn photo-section []
  (let [photo-url @(rf/subscribe [::photo-subs/photo-url])]

    [:div
     [:img {:src photo-url
            :on-load #(rf/dispatch [::photo-events/set-photo-loading-status :loaded])
            :style {:max-width "80%"
                    :max-height "80%"
                    :position "relative"
                    :left "50%"
                    :margin-top "5%"
                    :transform "translateX(-50%)"
                    :object-position "center top"}}]
     
     [:div {:style {:width "100%"
                    :display "flex"
                    :align-items "center"
                    :justify-content "center"}}
      [button [::photo-events/clear-photo] "retake-photo" "retake?"]
      [button [::photo-events/send-photo] "send-photo" "send?"]]]))




