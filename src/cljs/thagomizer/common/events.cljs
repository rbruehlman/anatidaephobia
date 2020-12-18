(ns thagomizer.common.events
  (:require
   [re-frame.core :as rf]
   [thagomizer.db :as db]
   [ajax.core :as ajax]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
