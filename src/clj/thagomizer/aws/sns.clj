(ns thagomizer.aws.sns
  (:require [cognitect.aws.client.api :as aws]))


(def sns (aws/client {:api :sns}))

(def topic {:b "arn:aws:sns:us-east-1:523586208714:google-meet"
            :c "arn:aws:sns:us-east-1:523586208714:anatidaephobia"})

(defn send-sms [uid message]
  (let [arn (uid topic)]
    (aws/invoke sns {:op :Publish :request
                     {:TopicArn arn
                      :Message message}})))