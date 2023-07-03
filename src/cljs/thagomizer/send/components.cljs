(ns thagomizer.send.components
  (:require
   [thagomizer.common.components.input :refer [button target-value]]
   [thagomizer.send.events :as events]
   [thagomizer.send.subs :as subs]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.receipt.components :as receipt-components]
   [thagomizer.receipt.subs :as receipt-subs]
   [thagomizer.receipt.events :as receipt-events]
   [thagomizer.entry.subs.authentication :as auth-subs]))


(defn on-text-field-value-change
  "Sets the text field with the value submitted in the event"
  [e]
  (rf/dispatch [::events/update-text-field (target-value e)]))

(defn last-message
  "Component to show the last message"
  []
  (let [last-msg (last @(rf/subscribe [::receipt-subs/messages]))]
    (when (not (nil? last-msg))
      [receipt-components/message-container-div last-msg]))
  )

(defn- show-message []
  (
   (rf/dispatch [::events/set-visibility])
   (rf/dispatch [::receipt-events/get-messages 1])
   )
  )

(defn last-message-div []
  (let [admin? @(rf/subscribe [::auth-subs/admin-status])
        visible? @(rf/subscribe[::subs/visibility])]
    (when (not admin?)
      (if visible?
        [button  nil "last-msg" "Show message?" :elm-overrides
         {:visible (str visible?)
          :onClick show-message}]
        [last-message]))))

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

(defn on-img-change [e]
  (let [^js/File file (first (-> e .-target .-files))]
    (rf/dispatch [::events/upload-photo file])))

(defn img-button []
  [:label {:style {:font-size 11
                   :font-family "Gloria Hallelujah, cursive"
                   :border "none"
                   :text-align "center"
                   :border-radius "15px"
                   :padding "7px 8px 7px 8px"
                   :margin "5px"
                   :display "inline-block"
                   :background-color "#EFEFEF"
                   :font-weight 400}}
   [:input {:type "file"
            :accept "image/*"
            :style {:display "none"}
            :on-change #(on-img-change %)}]
   "snap?"
   ])

(defn send-app []
  (r/create-class
     {
   :reagent-render
      (fn []
  [:<>
   [:div {:style {:margin "10px auto"}}
    [last-message-div]]
   [:div {:key "input-text-field"}
    [input-text-field]]
   [:div {:style {:margin "10px auto"}
          :key "buttons"}
    [img-button]
    [button [::events/send-message] "sms" "moo?"]
    ]])}
))