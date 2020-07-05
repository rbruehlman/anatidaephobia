(ns thagomizer.subs.core
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::text-field
 (fn [db]
   (:text-field db)))

(rf/reg-sub
 ::newest-message
 (fn [db]
   (:newest-message db)))