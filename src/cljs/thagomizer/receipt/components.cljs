(ns thagomizer.receipt.components
  (:require [thagomizer.receipt.subs :as receipt-subs]
            [thagomizer.receipt.events :as receipt-events]
            [re-frame.core :as rf]
            [reagent.core :as reagent]
            [thagomizer.common.funcs :as f-utils]
            [thagomizer.common.components.utils :as c-utils]
            [thagomizer.entry.subs.authentication :as auth-subs]))

(defn message
  [timestamp text]
  [:div {:id timestamp}
   [:span {:class "text"} text]])

(defn make-paragraph [timestamp i p]
  [:p
   {:style {:margin-top "10px"}
    :key (str timestamp "-receipt-p" i)} p])

(defn message-div [messages]
  (for [msg messages]
    (let [timestamp (:timestamp msg)
          paragraphs (map-indexed vector (c-utils/split-paragraph (:message msg)))]
      [:div.rows {:key (str timestamp "-receipt-row")
                  :style {:vertical-align "bottom"
                          :padding-bottom "10px"}}
       [:div {:key (str timestamp "-receipt-timestamp")
              :style {:margin-top "10px"}}
        [:span {:class "is-2"
                :key (str timestamp "-receipt-timestamp-sp")
                :style {:font-size 12
                        :text-align "left"
                        :color "blue"}}
         (f-utils/convert-to-human-time timestamp "dddd h:mm A, YYYY-MM-DD")]]
       [:div.row {:class "is-8"
                  :style {:text-align "left"}
                  :key (str timestamp "-receipt-user")}
        (for [[i p] paragraphs]
          (make-paragraph timestamp i p))]])))

(def dark-blue "#00008b")

(defn divider [text]
  [:div {:style {:margin-top "10px"}}
   [:p {:style {:color dark-blue
               :font-size "18px"
               :margin 0
               :padding 0
               :align "left"
               :font-style "italic"}}
   text]
  [:hr {:style {:margin 0
                :padding 0
                :border-top "1px solid grey"}}]])

(defn messages []
    []
  (reagent/create-class
   {;; we want to scroll to the bottom every time a new message is posted
    :component-did-update (fn [] (rf/dispatch [::receipt-events/scroll-up]))
    
    :reagent-render
    (fn []
      (let [messages @(rf/subscribe [::receipt-subs/messages])
            new-msgs (filter #(:new %) messages)
            old-msgs (filter #(= false (:new %)) messages)]

        [:div {:style {:font-family "Roboto, sans-serif"
                       :width "100%"
                       :max-height "70vh"
                       :flex "1 1 auto"
                       :overflow-y "scroll"}
               :id "receipt-message-list"
               :key "receipt-message-list"}
         
         (when-not (empty? new-msgs)
           [:div
            (divider "new")
            (message-div new-msgs)])
         (when-not (empty? old-msgs)
           [:div
            (divider "read")
            (message-div old-msgs)])]))}))

(defn paginate-button [key max-page-count current-page page-num next-page]
  [:button {:key (if (integer? next-page) next-page key)
            :style
            {:font-family "Gloria Hallelujah, cursive"
             :font-size 11
             :border "none"
             :text-align "center"
             :padding "5px 7px 5px 7px"
             :margin "5px"
             :disabled (= max-page-count current-page)}
   :type "button"
   :value page-num
   :onClick #(rf/dispatch
              [::receipt-events/get-messages next-page])}
   page-num])

(defn get-range [current-page max-page-count]
  (let [min-num (if (> (- current-page 4) 1)
                  (- current-page 4)
                  1)
        max-num (if (> (+ min-num 9) max-page-count)
                  (+ 1 max-page-count)
                  (+ min-num 10))]
    
    (range min-num, max-num)))

(defn paginate-buttons []
  (let [current-page @(rf/subscribe [::receipt-subs/current-page])
        max-page-count @(rf/subscribe [::receipt-subs/page-count])]
    
    [:div {:style c-utils/center-css}
     [paginate-button "start" max-page-count current-page "<<" 1]
     (for [page-num (get-range current-page max-page-count)]
       [paginate-button page-num max-page-count current-page page-num page-num])
     [paginate-button "end" max-page-count current-page ">>" max-page-count]]))

(defn receipt-app []
      (reagent/create-class

     {:component-did-mount
      (fn []
        (rf/dispatch [::receipt-events/get-messages 1])
        (if-not @(rf/subscribe [::auth-subs/admin-status])
          (rf/dispatch [::receipt-events/register-visit])))

      :reagent-render
       (fn []
         [:div [messages]
          [paginate-buttons]
          ])}))