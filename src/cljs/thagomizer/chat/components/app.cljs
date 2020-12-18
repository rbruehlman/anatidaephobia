(ns thagomizer.chat.components.app
  (:require
   [reagent.core :as reagent]
   [reagent.dom :as rdom]
   [thagomizer.chat.components.accents :refer [online-users]]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.common.components.sms :refer [sms-button]]
   [thagomizer.chat.components.messages :refer [messages]]
   [thagomizer.chat.components.typing :refer [typing-indicator]]
   [thagomizer.chat.events.sms :as sms-events]
   [thagomizer.chat.components.input :refer [input-text-field]]
   [thagomizer.chat.components.camera :refer [camera-modal]]))


(defn chat-app []
  (let [state (reagent/atom {})]

    (reagent/create-class

     {:component-did-mount
      (fn [this]
        (let [dom-rect (.getBoundingClientRect (rdom/dom-node this))]
          (c-utils/update-dimensions state dom-rect)))

      :component-did-update
      (fn [this]
        (let [dom-rect (.getBoundingClientRect (rdom/dom-node this))]
          (c-utils/update-dimensions state dom-rect)))

      :reagent-render
      (fn []
        [:<>
         [messages atom]
         ;;[camera-modal state]
         [:div {:key "typing-indicator"}
          [typing-indicator]]
         [:div {:key "input-text-field"}
          [input-text-field]]
         [:div {:key "online-users"}
          [online-users]]
         [:div {:style {:margin "0 auto"}
                :key "buttons"}
          ;;[img-button]
          [sms-button [::sms-events/send-sms]]]])})))
