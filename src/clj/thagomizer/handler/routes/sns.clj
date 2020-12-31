(ns thagomizer.handler.routes.sns
  (:require [ring.util.response :as response]
            [thagomizer.aws.sns :as aws-sns]))

(defn sns-handler [ring-req]
  (let [result (aws-sns/send-sms :b "Moo from Thagomizer?")]

    (if (get result :MessageId)
      (response/status 200)
      (response/status 500))))
