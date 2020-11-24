(ns thagomizer.components.messages
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [thagomizer.subs.core :as subs]
   [thagomizer.events.messages :as message-events]
   [thagomizer.components.utils :as c-utils]
   [cljsjs.moment]))

(defn message
  "Div for displaying the UID author and message"
  [timestamp uid text]
  [:div {:id timestamp}
   [:span {:class "uid"} uid]
   [:span {:class "text"} text]])

(defn convert-to-human-time
  "Display time in h:mm A format, e.g. 10:23 PM.
   The try/catch here is because there were invalid dates for some reason?"
  [unix-time]
  (try
    (.format (js/moment unix-time) "h:mm A")
    (catch :default e
      e)))

(defn messages
  "Component to populate formatted divs for each message (author, time, and text)"
  []
  (reagent/create-class
   {;; we want to scroll to the bottom every time a new message is posted
    :component-did-update (fn [] (rf/dispatch [::message-events/scroll-down]))

    :display-name "message-list"

    :reagent-render
    (fn []
      (let [messages @(rf/subscribe [::subs/latest-messages])]
        [:div {:style {:font-family "Roboto, sans-serif"
                       :width "100%"
                       :flex "1 1 auto"
                       :overflow "auto"}
               :id "message-list"}
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
                             :color (:color msg)
                             :padding-left 10
                             :text-align "right"}}
              (c-utils/trunc-uid (:author msg))]]
            [:div.row {:class "is-8"
                       :style {:text-align "left"}}
             (for [p (c-utils/split-paragraph (:msg msg))]
               [:p {:key (str (:timestamp msg) "-p")} p])]])]))}))