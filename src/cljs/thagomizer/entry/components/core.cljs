(ns thagomizer.entry.components.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.common.components.accents :refer [header]]
   [thagomizer.entry.components.passcode :refer [input-passcode-field]]
   [thagomizer.entry.subs.authentication :as authentication-subs]
   [thagomizer.chat.components.app :refer [chat-app]]
   [thagomizer.send.components :refer [send-app]]
   [thagomizer.receipt.components :refer [receipt-app]]))


(defn larson []
  [:<>
   [header]
   [:img {:src "https://berthoudsurveyor.com/wp-content/uploads/2018/04/thagomizer.jpg"
          :style {:width "50%"
                  :margin "auto"}}]])

(defn out-of-order []
  [:<>
   [header]
   [:p {:style (merge {:font-size 25} c-utils/center-css)} "Check back next week!"]
   [:img {:src "https://i.pinimg.com/474x/da/b4/8c/dab48ccccd222438e2abdf98e3d284df--comic-book.jpg"
          :style {:width "50%"
                  :margin "auto"}}]])

(defn app []
  (let [authenticated  @(rf/subscribe [::authentication-subs/authentication])
        auth-chat      @(rf/subscribe [::authentication-subs/authorized-mode :chat])
        auth-receipt   @(rf/subscribe [::authentication-subs/authorized-mode :receipt])
        auth-send      @(rf/subscribe [::authentication-subs/authorized-mode :send])]
    [:div#flex-container
     {:style (merge {:width "60%"
                     :flex-flow "column"
                     :display "flex"
                     :align-items "stretch"
                     :height "90vh"} c-utils/center-css)}
     (cond
       (false? authenticated)
       [larson]
       (true? auth-chat)
       [chat-app]
       (true? auth-receipt)
       [receipt-app]
       (true? auth-send)
       [out-of-order]
       :else
       [:<>
        [header]
        [input-passcode-field]])]))