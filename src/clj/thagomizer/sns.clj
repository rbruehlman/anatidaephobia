(ns thagomizer.sns
  (:require [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]))

(def sns (aws/client {:api :sns
                      :credentials-provider (credentials/basic-credentials-provider
                                             {:access-key-id     (System/getenv "AWS_ACCESS_KEY")
                                              :secret-access-key (System/getenv "AWS_SECRET_ACCESS_KEY")})}))

(defn send-sms [ring-req]
  (aws/invoke sns {:op :Publish :request {:TopicArn (System/getenv "TOPIC_ARN")
                                          :Message "Moo from Thagomizer?"}}))