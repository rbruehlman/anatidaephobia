(ns thagomizer.utils
  (:require
   [ring.middleware.defaults]))

(defn ip-address [ring-req]
  ;;remote-addr
  (:client-id ring-req))

(defn now-unixtime []
  (.getTime (java.util.Date.)))