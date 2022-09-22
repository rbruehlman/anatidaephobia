(ns thagomizer.redis
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def redis-conn {:pool {} :spec {:uri "redis://localhost:6379/"}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(defn save-draft [data]
  (wcar*
   (car/set "draft" data)))

(defn get-draft []
  (wcar*
   (car/get "draft")))