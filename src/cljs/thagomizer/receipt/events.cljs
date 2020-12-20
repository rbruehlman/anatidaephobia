(ns thagomizer.receipt.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [thagomizer.receipt.queries :as q]
   [thagomizer.common.funcs :refer [?csrf-token]]))

(rf/reg-event-fx
 ::scroll-up
 (fn [cofx]
   (let [message-list (.getElementById js/document "receipt-message-list")]
     (.scrollTo message-list 0 0))))

(rf/reg-event-fx
 ::get-messages
 (defn get-messages [_ [_ page]]
     {:http-xhrio {:method :get
                   :uri "/messages"
                   :params {:page page}
                   :format (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [::handle-msg-success]
                   :on-failure [::handle-msg-failure]}}))

(rf/reg-event-db
 ::handle-msg-success
 (fn [db [_ response]]
   (q/set-messages db response)))

(rf/reg-event-db
 ::handle-msg-failure
 (fn [db [_ response]]
   (q/set-message-error db response)))

(rf/reg-event-fx
 ::register-visit
 (defn register-visit []
   {:http-xhrio {:method :post
                 :uri "/visits"
                 :headers {:X-CSRF-Token ?csrf-token}
                 :format (ajax/json-request-format)
                 :response-format (ajax/text-response-format)
                 :on-success [::handle-visit-success]
                 :on-failure [::handle-visit-failure]}}))

(rf/reg-event-db
 ::handle-visit-success
 (fn [] ))

(rf/reg-event-fx
 ::handle-visit-failure
 (fn []
   (js/alert "Failed to register visit!")))