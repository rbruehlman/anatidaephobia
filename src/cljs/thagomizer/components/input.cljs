(ns thagomizer.components.input
  (:require
   [re-frame.core :as rf]
   [thagomizer.events.input :as input-events]
   [thagomizer.subs.core :as subs]
   [thagomizer.components.utils :as c-utils]))

(defn target-value
  "Extracts the value of the event fired."
  [event]
  (.-value (.-target event)))

(defn on-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::input-events/update-text-field (target-value e)]))

(defn handle-enter-press
  "We want to send the message when the user hits enter, but add a 
   paragraph when they hit shift+enter.  Otherwise, behave as normal!"
  [e]
  (let [key-num (.-which e)
        shift   (.-shiftKey e)
        enter   13]
    (cond
      (and (= enter key-num) shift)
      (do
        (.preventDefault e)
        (rf/dispatch [::input-events/update-text-field (str (target-value e) "\n")]))
      (= enter key-num)
      (do (.preventDefault e)
          (rf/dispatch [::input-events/submit-message]))
      :else
      "default")))

(defn input-text-field
  "Component for the input text field"
  []
  (let [text-field @(rf/subscribe [::subs/text-field])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::input-events/submit-message]))}
     [:div [:textarea {:name :text-field
                       :minLength 1
                       :value text-field
                       :wrap "soft"
                       :on-change #(on-value-change %)
                       :on-key-down #(handle-enter-press %)
                       :style (merge {:min-height "100px"
                                      :width "100%"
                                      :resize "none"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"
                                      :display "flex"
                                      :overflow-y "scroll"}
                                     c-utils/center-css)}]]]))