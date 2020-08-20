(ns thagomizer.components.sms
  (:require
   [re-frame.core :as rf]
   [thagomizer.events.sms :as sms-events]))

(defn sms-button
  "Send an SMS message"
  []
  [:button {:style {:font-size 10
                    :border "none"
                    ;;:background-color "white"
                    :text-align "center"
                    :display "inline-block"}
            :type "submit"
            :value "moo?"
            :onClick #(rf/dispatch [::sms-events/send-sms])}])