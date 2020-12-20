(ns thagomizer.send.components
  (:require
   [thagomizer.common.components.sms :refer [sms-button]]
   [thagomizer.send.events :as events]
   [thagomizer.send.subs :as subs]
   [thagomizer.common.components.input :refer [target-value]]
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]))


(defn on-text-field-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::events/update-text-field (target-value e)]))

(defn input-text-field
  "Component for the input text field"
  []
  (let [text-field @(rf/subscribe [::subs/text-field])]
     [:div [:textarea {:name :text-field
                       :minLength 1
                       :value text-field
                       :wrap "soft"
                       :on-change #(on-text-field-value-change %)
                       :style (merge {:min-height "70vh"
                                      :width "100%"
                                      :resize "none"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"
                                      :display "flex"
                                      :overflow-y "scroll"
                                      :margin-bottom "10px"}
                                     c-utils/center-css)}]]))

(defn send-app []
  [:<>
   [:div {:key "input-text-field"}
    [input-text-field]]
   [:div {:style {:margin "0 auto"}
          :key "buttons"}
    [sms-button [::events/send-message]]
    ]])
