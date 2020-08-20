(ns thagomizer.components.accents
  (:require
   [re-frame.core :as rf]
   [thagomizer.subs.core :as subs]
   [thagomizer.components.utils :as c-utils]
   [thagomizer.events.messages :as message-events]))

(defn header []
  [:h1
   {:style (merge {:font-family "Gloria Hallelujah, cursive"
                   :font-size "4vmax"
                   :display "flex"
                   :padding "20px 0 20px 0px"
                   :text-align "center"}
                  c-utils/center-css)}
   "THE THAGOMIZER"])

(defn online-users
  "Display a circle each online user, with the appropriate color."
  []
  (let [uids @(rf/subscribe [::subs/uids])]
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


(defn clear-button
  "Clear messages"
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
            :value "reset?"
            :onClick #((rf/dispatch [::message-events/clear-messages]))}
   "reset"])