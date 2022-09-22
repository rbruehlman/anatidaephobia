(ns thagomizer.redis
  (:require [taoensso.carmine :as car :refer (wcar)]))

;; change to redis://localhost:6379/ if running locally
(def redis-conn {:pool {} :spec {:uri "redis://redis:6379/"}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(defn save-draft [data]
  (wcar*
   (car/set "draft" data)))

(defn get-draft []
  (wcar*
   (car/get "draft")))