(ns thagomizer.chat.events.sms
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [thagomizer.common.funcs :refer [?csrf-token]]))

(rf/reg-event-fx
 ::send-sms
 (fn []
     {:http-xhrio {:method :post
                   :uri "/sms"
                   :headers {:X-CSRF-Token ?csrf-token}
                   :params {:uid "b"}
                   :format (ajax/json-request-format)
                   :response-format (ajax/text-response-format)
                   :on-success [::handle-sms-success]
                   :on-failure [::handle-sms-failure]}}))

(rf/reg-event-fx
 ::handle-sms-success
 (fn []
   (js/alert "Moo!")))

(rf/reg-event-fx
 ::handle-sms-failure
 (fn [_ [_ response]]
   (js/alert (str "Houston, we have a problem..." response))))
