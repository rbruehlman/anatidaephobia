(ns thagomizer.handler.routes.sns
  (:require [cognitect.aws.client.api :as aws]
            [ring.util.response :as response]
            [thagomizer.utils :refer [json-to-map]]))

(def sns (aws/client {:api :sns}))

(def topic {:b "arn:aws:sns:us-east-1:523586208714:google-meet"
            :c "arn:aws:sns:us-east-1:523586208714:google-meet"})

(defn send-sms [uid message]
  (let [arn (uid topic)]
    (aws/invoke sns {:op :Publish :request
                          {:TopicArn arn
                           :Message message}})))

(defn sns-handler [ring-req]
  (let [resp (json-to-map :ring-req)
        uid  (:keyword (:uid resp))
        result (send-sms uid "Moo from Thagomizer?")]

    (if (get result :MessageId)
      (response/status 200)
      (response/status 500))))

