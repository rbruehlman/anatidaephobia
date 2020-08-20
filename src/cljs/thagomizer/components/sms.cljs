(ns thagomizer.components.sms
  (:require
   [re-frame.core :as rf]
   [thagomizer.events.sms :as sms-events]))

(defn sms-button
  "Send an SMS message"
  []
  [:button {:style {:font-size 11
                    :font-family "Gloria Hallelujah, cursive"
                    :border "none"
                    :text-align "center"
                    :border-radius "15px"
                    :padding "5px 8px 5px 8px"
                    :margin "5px"}
            :type "submit"
            :value "moo?"
            :onClick #(rf/dispatch [::sms-events/send-sms])}
   "moo?"])