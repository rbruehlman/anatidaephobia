(ns thagomizer.send.components
  (:require
   [thagomizer.common.components.sms :refer [sms-button]]
   [thagomizer.common.components.input :refer [input-text-field]]
   [thagomizer.send.events :as events]
   [thagomizer.send.subs :as subs]))

(defn send-app []
   [:<>
    [:div {:key "input-text-field"}
    [input-text-field
     ::subs/text-field
     ::events/update-text-field
     ::events/submit-message
     {:height "70vh"}]]
   [:div {:style {:margin "0 auto"}
          :key "buttons"}
    [sms-button [::events/send-sms]]]])
