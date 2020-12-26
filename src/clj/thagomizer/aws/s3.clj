(ns thagomizer.aws.s3
  (:require [cognitect.aws.client.api :as aws]
            [clojure.string :as str])
  (:import com.gkarthiks.S3PresignedURL))


(def s3 (aws/client {:api :s3 :region "us-east-1"}))

(defn get-extension [ring-req]
  (second
   (str/split
    (get-in ring-req [:multipart-params "file" :content-type])
    #"/")))

(defn create-key [ext]
  (str "images/" (.getTime (java.util.Date.)) "." ext))

(def s3-creds {S3PresignedURL/AWS_ACCESS_ID
               (System/getenv "AWS_ACCESS_KEY_ID")
               S3PresignedURL/AWS_SECRET_KEY
               (System/getenv "AWS_SECRET_ACCESS_KEY")})

(defn get-presigned-url [key]
  (let [bucket (System/getenv "S3_BUCKET")
        url (str "https://" bucket ".s3.amazonaws.com/" key)]
    (S3PresignedURL/getS3PresignedURL url s3-creds "GET" "us-east-1" 3600)))

(defn upload-img-to-s3 [key img]
  (let [bucket (System/getenv "S3_BUCKET")]
    
  (aws/invoke s3 {:op :PutObject
                  :request {:Bucket bucket
                            :Key key
                            :Body img}})))