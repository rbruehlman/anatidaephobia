(ns thagomizer.handler.routes.messages
  (:require
   [thagomizer.db.queries :as q]
   [cheshire.core :refer [generate-string]]
   [thagomizer.utils :refer [json-to-map]]
   [thagomizer.handler.routes.utils :as utils]
   [thagomizer.aws.s3 :as s3]
   [thagomizer.redis :as redis]
   [ring.util.request :refer [body-string]]))


(defn new-message-handler [ring-req]
  (let [req   (json-to-map ring-req)
        admin  (:admin req)
        data   (:data req)]

    (if admin
      (utils/send-message utils/send-admin-message data)
      (utils/send-message utils/send-nonadmin-message data))))

(defn save-draft-handler [ring-req]
  (let [draft   (body-string ring-req)]

    (redis/save-draft draft)))

(defn get-draft-handler [ring-req]
  {:status  200
   :headers {"Content-Type" "text/plain charset=utf-8"}
   :body    (generate-string (redis/get-draft))}
  )

(defn add-presigned-url [messages]
  (map #(if (utils/is-s3-image? (:message %))
                (update % :message s3/get-presigned-url)
                %) messages))

(defn display-message-handler [ring-req]
  (let [page (Integer/parseInt (get-in ring-req [:params :page]))
        messages (add-presigned-url (q/get-messages page))]
    
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (generate-string messages
                               {:key-fn (fn [k]
                                          (name k))})}))
