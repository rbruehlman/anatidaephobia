(ns thagomizer.common.components.input
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]))

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
  [update-event e]
  (rf/dispatch [update-event (target-value e)]))

(defn input-text-field
  "Component for the input text field"
  [submit-sub update-event submit-event style-overrides]
  (let [text-field @(rf/subscribe [submit-sub])
        style-overrides (or style-overrides {})]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [submit-sub]))}
     [:div [:textarea {:name :text-field
                       :minLength 1
                       :value text-field
                       :wrap "soft"
                       :on-change #((partial on-text-field-value-change update-event %))
                       :on-key-down #(handle-enter-press % update-event
                                                           submit-event)
                       :style (merge {:min-height "100px"
                                      :width "100%"
                                      :resize "none"
                                      :font-size "16px"
                                      :font-family "Roboto, sans-serif"
                                      :display "flex"
                                      :overflow-y "scroll"}
                                     c-utils/center-css
                                     style-overrides)}]]]))
