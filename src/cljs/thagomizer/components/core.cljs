(ns thagomizer.components.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [thagomizer.events.core :as events]
   [thagomizer.subs.core :as subs]
   [cljsjs.moment]
   [clojure.string :refer [join]]))

(defn target-value [event]
  (.-value (.-target event)))

(defn on-value-change [e]
  (rf/dispatch [::events/update-text-field (target-value e)]))


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

(defn handle-enter-press [e]
  (let [key-num (.-which e)
        shift   (.-shiftKey e)
        enter   13]
    (cond
     (and (= enter key-num) shift)
      (do
        (.preventDefault e)
        (rf/dispatch [::events/update-text-field (str (target-value e) "\n")]))
     (= enter key-num)
      (do (.preventDefault e)
          (rf/dispatch [::events/submit-message]))
      :else
      "default")))

(def center-css {:margin "auto"
                 :justify-content "center"
                 :display "flex"
                 :align-items "center"})

(defn text-field-field []
  (let [text-field @(rf/subscribe [::subs/text-field])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::events/submit-message]))
            :style {:margin 10}}
     [:div [:textarea {:name :text-field
                       :minLength 1
                       :value text-field
                       
                       :wrap "soft"
                       :on-change #(on-value-change %)
                       :on-key-down #(handle-enter-press %)
                       :style (merge {:width "40%"
                                      :min-height "100px"
                                      :resize "none"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"}
                                     center-css)}]]]))

(defn is-typing-text [users-typing]
  (let [user-count (count users-typing)]
    (str (cond
           (>= user-count 3)
           (str (join ", "
                      (update users-typing
                              (- user-count 1)
                              #(str "and " %))) " are typing...")
           
           (= user-count 2)
           (str (join " and " users-typing) " are typing...")
           
           :else ;;technically i guess no one could be typing ¯ \_(ツ)_/¯
           (str (join " and " users-typing) " is typing...")))))

(defn typing-indicator []
  (let [users-typing @(rf/subscribe [::subs/typing-status])]
    (if (not (empty? users-typing))
      [:div
       [:div#typing-status
        {:style (merge {:font-family "Roboto, sans-serif"
                        :font-style "italic"} center-css)}
        (is-typing-text users-typing)]]
      [:div
       [:div#typing-status
        {:style (merge {:visibility "hidden"} center-css)}
        "No one is typing, so this is hidden :("]])))

(defn header []
  [:h1
   {:style (merge {:font-family "Gloria Hallelujah, cursive"} center-css)}
   "THE THAGOMIZER"])