(ns thagomizer.chat.components.messages
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [thagomizer.chat.subs.messages :as message-subs]
   [thagomizer.chat.events.messages :as message-events]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.common.funcs :as f-utils]))

(defn message
  "Div for displaying the UID author and message"
  [timestamp uid text]
  [:div {:id timestamp}
   [:span {:class "uid"} uid]
   [:span {:class "text"} text]])

(defn- msg-type-style
  [{:keys [type]}]
  (cond
    (= type "text") "regular"
    (= type "exit") "italic"))

(defn messages
  "Component to populate formatted divs for each message (author, time, and text)"
  []
  (reagent/create-class
   {;; we want to scroll to the bottom every time a new message is posted
    :component-did-update (fn [] (rf/dispatch [::message-events/scroll-down]))

    :display-name "message-list"

    :reagent-render
    (fn []
      (let [messages @(rf/subscribe [::message-subs/latest-messages])]
        [:div {:style {:font-family "Roboto, sans-serif"
                       :width "100%"
                       :flex "1 1 auto"
                       :overflow "auto"}
               :id "message-list"
               :key "message-list"}
         (for [msg messages]
           (let [timestamp (:timestamp msg)]
            [:div.rows {:key (str timestamp "-row")
                        :style {:vertical-align "bottom"
                                :padding-bottom "10px"}}
            [:div {:key (str timestamp "-timestamp")}
             [:span {:class "is-2"
                     :key (str timestamp "-timestamp-sp")
                     :style {:font-size 12
                             :text-align "left"}}
              (f-utils/convert-to-human-time timestamp "h:mm A")]
             [:span {:class "is-2"
                     :key (str timestamp "-user-sp")
                     :style {:font-size 12
                             :color (or (:color msg) "black")
                             :padding-left 10
                             :text-align "right"}}
              (c-utils/trunc-uid (:author msg))]]
            [:div.row {:class "is-8"
                       :style {:text-align "left"}
                       :key (str timestamp "-user")}
             (for [p (c-utils/split-paragraph (:msg msg))]
               [:p {:key (str timestamp "-p")
                    :style {:font-style (msg-type-style msg)}} p])]]))]))}))