(ns thagomizer.components.messages
  (:require
   [re-frame.core :as rf]
   [thagomizer.subs.core :as subs]
   [thagomizer.components.utils :as c-utils]
   [cljsjs.moment]))

(defn message [timestamp uid text]
  [:div {:id timestamp}
   [:span {:class "user"} uid]
   [:span {:class "text"} text]])

(defn convert-to-human-time [unix-time]
  (.format (js/moment unix-time) "MMMM Do YYYY, h:mm A"))

(defn messages []
  (let [messages @(rf/subscribe [::subs/latest-messages])]
    [:div
     (for [msg messages]
       [:div {:key (:timestamp msg)}
        [:span (:uid msg)]
        [:span (convert-to-human-time (:timestamp msg))]
        [:span (:msg msg)]])]))