(ns thagomizer.receipt.components
  (:require [thagomizer.receipt.subs :as receipt-subs]
            [thagomizer.receipt.events :as receipt-events]
            [re-frame.core :as rf]
            [thagomizer.common.funcs :as f-utils]
            [thagomizer.common.components.utils :as c-utils]))

(defn message
  [timestamp text]
  [:div {:id timestamp}
   [:span {:class "text"} text]])

(defn messages []
  (let [messages @(rf/subscribe [::receipt-subs/messages])]
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
            (f-utils/convert-to-human-time timestamp)]]
          [:div.row {:class "is-8"
                     :style {:text-align "left"}
                     :key (str timestamp "-user")}
           (for [p (c-utils/split-paragraph (:msg msg))]
             [:p {:key (str timestamp "-p")} p])]]))]))

(defn paginate-button [key max-page-count current-page page-num next-page]
  [:button {:key key
            :style
            {:font-family "Gloria Hallelujah, cursive"
             :font-size 11
             :border "none"
             :text-align "center"
             :padding "5px 7px 5px 7px"
             :margin "5px"
             :disabled (= max-page-count current-page)}}
   :type "button"
   :value page-num
   :onClick #(rf/dispatch
              [::receipt-events/messages next-page])
   page-num])

(defn paginate-buttons []
  (let [current-page @(rf/subscribe [::receipt-subs/current-page])
        max-page-count @(rf/subscribe [::receipt-subs/page-count])
        limit-page-count (if (< max-page-count (+ 9 current-page)) 
                           max-page-count
                           (+ 9 current-page))]
    [:div
     [paginate-button "start" max-page-count current-page "<<" 1]
     (for [page-num (range current-page (+ 1 limit-page-count))]
       [paginate-button page-num max-page-count current-page page-num (+ 1 page-num)]
     )
     [paginate-button "end" max-page-count current-page ">>" max-page-count]]))

(defn receipt-app []
  [:div [messages]
        [paginate-buttons]])