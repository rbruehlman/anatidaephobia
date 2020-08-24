(ns thagomizer.sns
  (:require [cognitect.aws.client.api :as aws]
            [ring.util.response :as response]))

(def sns (aws/client {:api :sns}))

(defn send-sms []
  (let [result (aws/invoke sns {:op :Publish :request {:TopicArn "arn:aws:sns:us-east-1:523586208714:google-meet"
                                                       :Message "Moo from Thagomizer?"}})]
  (if (get result :MessageId)
    (response/status 200)
    (response/status 500))))