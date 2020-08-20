(ns thagomizer.events.sms
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]))

(rf/reg-event-fx
 ::send-sms
 (fn []
     {:http-xhrio {:method :post
                   :uri "/sms"
                   :format (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [::handle-sms-success]
                   :on-failure [::handle-sms-failure]}}))

(rf/reg-event-fx
 ::handle-sms-success
 (fn []
   (js/alert "Moo!")))

(rf/reg-event-fx
 ::handle-sms-failure
 (fn []
   (js/alert "Houston, we have a problem...")))
