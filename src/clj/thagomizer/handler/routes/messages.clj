(ns thagomizer.handler.routes.messages
  (:require
   [thagomizer.db.queries :as q]
   [cheshire.core :refer [generate-string]]
   [thagomizer.utils :refer [json-to-map]]
   [thagomizer.handler.routes.sns :as sns]
   [ring.util.response :as response]))

(defn send-nonadmin-message [data]
  (sns/send-sms :b data))

(defn send-admin-message [data]
  (let [sent-already? (q/get-last-message-timestamp)
        prompt (if sent-already?
                 (q/get-last-prompt)
                   (q/get-next-prompt))]
    
    (q/insert-message data (:id prompt))
    
    (if-not sent-already?
      (sns/send-sms :c (:prompt prompt))
      (response/status 200))))

(defn send-message [func data]
  (let [resp (func data)]
    (if (or (contains? resp :MessageId) (= (:status resp) 200))
    (response/status 200)
    (response/status 500))))

(defn new-message-handler [ring-req]
  (let [req   (json-to-map ring-req)
        admin  (:admin req)
        data   (:data req)]

    (if admin
      (send-message send-admin-message data)
      (send-message send-nonadmin-message data))))

(defn display-message-handler [ring-req]
  (let [page (Integer/parseInt (get-in ring-req [:params :page]))
        messages (q/get-messages page)]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (generate-string messages
                               {:key-fn (fn [k]
                                          (name k))})}))
