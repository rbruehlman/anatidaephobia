(ns thagomizer.entry.components.passcode
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.components.input :refer [target-value, handle-enter-press]]
   [thagomizer.entry.subs.authentication :as auth-subs]
   [thagomizer.entry.events.authentication :as auth-events]
   [thagomizer.common.components.utils :as c-utils]))

(defn on-passcode-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::auth-events/update-passcode-field (target-value e)]))

(defn input-passcode-field
  "Component for the input text field"
  []
  (let [passcode @(rf/subscribe [::auth-subs/passcode])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::auth-events/submit-passcode]))}
     [:div [:input {:name :passcode-field
                    :minLength 1
                    :value passcode
                    :wrap "soft"
                    :on-change #(on-passcode-value-change %)
                    :on-key-down #(handle-enter-press % ::auth-events/update-passcode-field
                                                        ::auth-events/submit-passcode)
                    :style (merge {:min-height "10px"
                                   :width "30%"
                                   :font-size "16px"
                                   :font-family "Roboto, sans-serif"
                                   :display "flex"}
                                  c-utils/center-css)}]]]))