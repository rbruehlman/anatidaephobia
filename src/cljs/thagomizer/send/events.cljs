(ns thagomizer.send.events
  (:require
   [clojure.string :as str]
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [thagomizer.send.queries :as q]
   [thagomizer.entry.queries.authentication :as auth-q]
   [thagomizer.common.funcs :refer [?csrf-token]]
   [thagomizer.common.events :refer [generate-form-data]]))


(rf/reg-event-db
 ::update-text-field
 (fn [db [_ value]]
   (q/set-text-field db value)))

(rf/reg-event-fx
 ::send-message
 (fn [cofx [_]]
   (let [db  (:db cofx)
         msg (q/get-text-field db)
         admin (auth-q/get-admin-status db)]
     
     (if (str/blank? msg)
       (js/alert "Can't send a blank message!")
       {:http-xhrio {:method :post
                     :uri "/message"
                     :params {:data msg :admin admin}
                     :headers {:X-CSRF-Token ?csrf-token}
                     :format (ajax/json-request-format)
                     :response-format (ajax/text-response-format)
                     :on-success [::handle-sms-success]
                     :on-failure [::handle-sms-failure]}}))))

(rf/reg-event-fx
 ::handle-sms-success
 (fn []
   (js/alert "Moo!")
   {:fx [[:dispatch [::update-text-field ""]]]}))

(rf/reg-event-fx
 ::handle-sms-failure
 (fn []
   (js/alert (str "Houston, we have a problem..."))))


(rf/reg-event-fx
 ::upload-photo
 (fn [cofx [_ img]]
   (let [db  (:db cofx)
         admin (auth-q/get-admin-status db)
         form-data (generate-form-data {:file img
                                        :admin admin})]
     
       {:http-xhrio {:method :post
                     :uri "/images"
                     :body form-data
                     :headers {:X-CSRF-Token ?csrf-token}
                     :format (ajax/json-request-format)
                     :response-format (ajax/text-response-format)
                     :on-success [::handle-img-success]
                     :on-failure [::handle-img-failure]}})))


(rf/reg-event-fx
 ::handle-img-success
 (fn [_]
   (js/alert "Snap!")))

(rf/reg-event-fx
 ::handle-img-failure
 (fn [_]
   (js/alert (str "Houston, we have a problem..."))))
