(ns thagomizer.handler.routes.sns
  (:require [cognitect.aws.client.api :as aws]
            [ring.util.response :as response]))

(def sns (aws/client {:api :sns}))

(def topic {:b "arn:aws:sns:us-east-1:523586208714:google-meet"
            :c "arn:aws:sns:us-east-1:523586208714:anatidaephobia"})

(defn send-sms [uid message]
  (let [arn (uid topic)]
    (aws/invoke sns {:op :Publish :request
                          {:TopicArn arn
                           :Message message}})))

(defn sns-handler [ring-req]
  (let [result (send-sms :b "Moo from Thagomizer?")]

    (if (get result :MessageId)
      (response/status 200)
      (response/status 500))))

