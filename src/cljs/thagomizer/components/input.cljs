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

(defn handle-enter-press
  "We want to send the message when the user hits enter, but add a 
   paragraph when they hit shift+enter.  Otherwise, behave as normal!"
  [e dispatch-shift-enter-event dispatch-enter-event]
  (let [key-num (or (.-keyCode e) (.-which e))
        shift   (.-shiftKey e)
        enter   13]
    (cond
      (and (= enter key-num) shift)
      (do
        (.preventDefault e)
        (rf/dispatch [dispatch-shift-enter-event (str (target-value e) "\n")]))
      (= enter key-num)
      (do (.preventDefault e)
          (rf/dispatch [dispatch-enter-event]))
      :else
      "default")))

(defn on-text-field-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::input-events/update-text-field (target-value e)]))

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

(defn on-passcode-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::input-events/update-passcode-field (target-value e)]))

(defn input-passcode-field
  "Component for the input text field"
  []
  (let [passcode @(rf/subscribe [::subs/passcode])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::input-events/submit-passcode]))}
     [:div [:input {:name :passcode-field
                       :minLength 1
                       :value passcode
                       :wrap "soft"
                       :on-change #(on-passcode-value-change %)
                       :on-key-down #(handle-enter-press % ::input-events/update-passcode-field
                                                           ::input-events/submit-passcode)
                       :style (merge {:min-height "10px"
                                      :width "30%"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"
                                      :display "flex"}
                                     c-utils/center-css)}]]]))