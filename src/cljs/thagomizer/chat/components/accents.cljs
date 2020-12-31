(ns thagomizer.chat.components.accents
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.subs.uids :as uid-subs]))



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