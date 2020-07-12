(ns thagomizer.components.input
  (:require
   [re-frame.core :as rf]
   [thagomizer.events.input :as input-events]
   [thagomizer.subs.core :as subs]
   [thagomizer.components.utils :as c-utils]))

(defn target-value [event]
  (.-value (.-target event)))

(defn on-value-change [e]
  (rf/dispatch [::input-events/update-text-field (target-value e)]))

(defn handle-enter-press [e]
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

(defn input-text-field []
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