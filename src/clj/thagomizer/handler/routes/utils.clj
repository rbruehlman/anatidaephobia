(ns thagomizer.handler.routes.utils
  (:require
   [thagomizer.db.queries :as q]
   [thagomizer.aws.sns :as sns]
   [ring.util.response :as response]
   [clojure.string :as str]
   [thagomizer.aws.s3 :as s3]))


(defn is-image? [msg]
  (str/includes? msg "images/"))

(defn send-nonadmin-message [msg]
  (q/insert-message (if (is-image? msg)
                                   (s3/get-presigned-url msg)
                                   msg) nil false)
  (sns/send-sms :b msg))

(defn send-admin-message [msg]
  (let [sent-already? (q/get-last-message-timestamp)
        prompt (if sent-already?
                 (q/get-last-prompt)
                 (q/get-next-prompt))]

    (q/insert-message msg (:id prompt) true)

    (if-not sent-already?
      (sns/send-sms :c (:prompt prompt))
      (response/status 200))))

(defn send-message [func msg]
  (let [resp (func msg)]
    (if (or (contains? resp :MessageId) (= (:status resp) 200))
      (response/status 200)
      (response/status 500))))