(ns thagomizer.components.messages
  (:require
   [re-frame.core :as rf]
   [thagomizer.subs.core :as subs]
   [cljsjs.moment]))

(defn message [timestamp uid text]
  [:div {:id timestamp}
   [:span {:class "uid"} uid]
   [:span {:class "text"} text]])

(defn convert-to-human-time [unix-time]
  (.format (js/moment unix-time) "h:mm A"))

(defn messages []
  (let [messages @(rf/subscribe [::subs/latest-messages])]
    [:div {:style {:font-family "Roboto, sans-serif"
                   :margin "auto"
                   :justify-content "center"
                   :align-items "center"
                   :width "100%"}}
     (for [msg messages]
       [:div.rows {:key (:timestamp msg)
                   :style {:vertical-align "bottom"
                           :padding-bottom "10px"}}
        [:div
         [:span {:class "is-2"
                 :style {:font-size 12
                         :text-align "left"}}
          (convert-to-human-time (:timestamp msg))]
         [:span {:class "is-2"
                 :style {:font-size 12
                         :color "red"
                         :padding-left 10
                         :text-align "right"}}
          (:uid msg)]]
        [:div.row {:class "is-8"
                   :style {:text-align "left"}}
         (:msg msg)]])]))