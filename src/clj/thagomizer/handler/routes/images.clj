(ns thagomizer.handler.routes.images
  (:require [thagomizer.aws.s3 :as s3]
            [thagomizer.handler.routes.utils :as utils]
            [clojure.java.io :as io]))
  
(defn- is-bool? [val]
  (boolean (Boolean/valueOf val)))

(defn image-handler [ring-req]
  (let [admin  (is-bool? (get-in ring-req [:multipart-params "admin"]))
        img (io/input-stream (get-in ring-req [:multipart-params "file" :tempfile]))
        key (s3/create-key (s3/get-extension ring-req))]
    
    (s3/upload-img-to-s3 key img)

    (if admin
      (utils/send-message utils/send-admin-message key)
      (utils/send-message utils/send-nonadmin-message key))))

