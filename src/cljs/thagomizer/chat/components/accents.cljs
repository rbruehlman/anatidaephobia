(ns thagomizer.chat.components.accents
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.uids :as uid-subs]
   [thagomizer.chat.events.camera :as camera-events]))



(defn online-users
  "Display a circle each online user, with the appropriate color."
  []
  (let [uids @(rf/subscribe [::uid-subs/uids])]
    [:div
     (for [[uid color] uids]
       [:span
        {:style {:height "5px"
                 :width "5px"
                 :background-color color
                 :border-radius "50%"
                 :display "inline-block"
                 :margin "10px 3px"}
         :key (str uid "-color")}])]))

(defn img-button
  []
  [:button {:style {:font-family "Gloria Hallelujah, cursive"
                    :font-size 11
                    :border "none"
                    :text-align "center"
                    :border-radius "15px"
                    :padding "5px 7px 5px 7px"
                    :margin "5px"
                    }
            :type "button"
            :value "paint?"
            :onClick #(rf/dispatch
                       [::camera-events/camera-stream-and-modal])}
   "paint"])