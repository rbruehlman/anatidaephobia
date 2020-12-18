(ns thagomizer.handler.routes.messages
  (:require
   [thagomizer.queries :as q]
   [cheshire.core :refer [generate-string]]
   [thagomizer.utils :refer [json-to-map]]
   [thagomizer.handler.routes.sns :as sns]))

(defn new-message-handler [ring-req]
  (let [data  (:data (json-to-map ring-req))
        prompt (q/get-next-prompt)]
    
    (q/insert-message data (:id prompt))
    (sns/send-sms :c (:prompt prompt))
    
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (generate-string data
                               {:key-fn (fn [k]
                                          (name k))})}))

(defn display-message-handler [ring-req]
  (let [page (or (get-in ring-req [:params :page]) 0)
        messages (q/get-messages page)]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (generate-string messages
                               {:key-fn (fn [k]
                                          (name k))})}))
