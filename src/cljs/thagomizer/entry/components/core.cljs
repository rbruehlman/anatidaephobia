(ns thagomizer.entry.components.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.entry.components.accents :refer [header]]
   [thagomizer.entry.components.passcode :refer [input-passcode-field]]
   [thagomizer.entry.subs.authentication :as authentication-subs]
   [thagomizer.chat.components.app :refer [chat-app]]
   [thagomizer.send.components :refer [send-app]]
   [thagomizer.receipt.components :refer [receipt-app]]))


(defn larson []
  [:img {:src "https://berthoudsurveyor.com/wp-content/uploads/2018/04/thagomizer.jpg"
         :style {:width "50%"
                 :margin "auto"}}])

(defn app []
  (let [authenticated  @(rf/subscribe [::authentication-subs/authentication])
        auth-chat      @(rf/subscribe [::authentication-subs/authorized-mode :chat])
        auth-receipt   @(rf/subscribe [::authentication-subs/authorized-mode :receipt])
        auth-send      @(rf/subscribe [::authentication-subs/authorized-mode :send])
        ]
    [:div#flex-container
     {:style (merge {:width "70%"
                     :flex-flow "column"
                     :display "flex"
                     :align-items "stretch"
                     :height "90vh"} c-utils/center-css)}
     [header {:style
              {:display "0 1 auto"}}]
     (cond
       (false? authenticated)
       [larson]
       (true? auth-chat)
       [chat-app]
       (true? auth-receipt)
       [receipt-app]
       (true? auth-send)
       [send-app]
       :else
       [input-passcode-field])]))