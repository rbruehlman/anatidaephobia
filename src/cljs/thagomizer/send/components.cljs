(ns thagomizer.send.components
  (:require
   [thagomizer.common.components.input :refer [button target-value]]
   [thagomizer.send.events :as events]
   [thagomizer.send.subs :as subs]
   [re-frame.core :as rf]
   [reagent.core :as reagent]
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
  [:<>
   [:div {:key "input-text-field"}
    [input-text-field]]
   [:div {:style {:margin "10px auto"}
          :key "buttons"}
    [img-button]
    [button [::events/send-message] "sms" "moo?"]
    ]])
