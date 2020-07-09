(ns thagomizer.events.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
