(ns thagomizer.chat.components.input
  (:require
   [thagomizer.common.components.input :refer [target-value handle-enter-press]]
   [thagomizer.chat.events.input :as input-events]
   [thagomizer.chat.subs.input :as input-subs]
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]))

(defn on-text-field-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::input-events/update-text-field (target-value e)]))

(defn input-text-field
  "Component for the input text field"
  []
  (let [text-field @(rf/subscribe [::input-subs/text-field])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::input-events/submit-message]))}
     [:div [:textarea {:name :text-field
                       :minLength 1
                       :value text-field
                       :wrap "soft"
                       :on-change #(on-text-field-value-change %)
                       :on-key-down #(handle-enter-press % ::input-events/update-text-field
                                                         ::input-events/submit-message)
                       :style (merge {:min-height "100px"
                                      :width "100%"
                                      :resize "none"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"
                                      :display "flex"
                                      :overflow-y "scroll"}
                                     c-utils/center-css)}]]]))