(ns thagomizer.utils
  (:require
   [ring.middleware.defaults]))

(defn ip-address [ring-req]
  (:remote-addr ring-req))

(defn now-time []
  (let [now (java.util.Date.)
        readable-format (java.text.SimpleDateFormat. "yyyy-MM-dd hh:mm:ss aa")]
    (.format readable-format now)))