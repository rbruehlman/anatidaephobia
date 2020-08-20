(ns thagomizer.components.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.components.accents :refer [header, online-users, clear-button]]
   [thagomizer.components.input :refer [input-text-field, input-passcode-field]]
   [thagomizer.components.messages :refer [messages]]
   [thagomizer.components.sms :refer [sms-button]]
   [thagomizer.components.typing :refer [typing-indicator]]
   [thagomizer.components.utils :as c-utils]
   [thagomizer.subs.core :as subs]))

(defn main-app []
  (list [messages]
        [:div
         [typing-indicator]]
        [:div
         [input-text-field]]
        [:div
         [online-users]]
        [:div {:style {:margin "0 auto"}}
         [clear-button]
         [sms-button]]))

(defn larson []
  [:img {:src "https://berthoudsurveyor.com/wp-content/uploads/2018/04/thagomizer.jpg"
         :style {:width "50%"
                 :margin "auto"}}])

(defn app []
  (let [passcode       @(rf/subscribe [::subs/passcode])
        authenticated  @(rf/subscribe [::subs/authentication])]
    [:div#flex-container
     {:style (merge {:width "70%"
                     :flex-flow "column"
                     :display "flex"
                     :align-items "stretch"
                     :height "90vh"} c-utils/center-css)}
     [header {:style
              {:display "0 1 auto"}}]
     (cond
       (true? authenticated)
       (main-app)
       (false? authenticated)
       (larson)
       :else
       (input-passcode-field))]))