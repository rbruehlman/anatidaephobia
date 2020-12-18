(ns thagomizer.receipt.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [thagomizer.receipt.queries :as q]))

(rf/reg-event-fx
 ::get-messages
 (defn get-messages
   ([]
    (get-messages [0]))
    ([page]
     {:http-xhrio {:method :get
                   :uri "/messages"
                   :params {:page page}
                   :format (ajax/json-request-format)
                   :response-format (ajax/text-response-format)
                   :on-success [::handle-success]
                   :on-failure [::handle-failure]}})))

(rf/reg-event-db
 ::handle-success
 (fn [db [_ response]]
   (q/set-messages db response)))

(rf/reg-event-db
 ::handle-failure
 (fn [db [_ response]]
   (q/set-message-error db response)))