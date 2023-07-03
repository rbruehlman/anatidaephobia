(ns thagomizer.send.subs
  (:require
   [re-frame.core :as rf]
   [thagomizer.send.queries :as q]))

(rf/reg-sub
 ::text-field
 (fn [db]
   (q/get-text-field db)))

(rf/reg-sub
 ::visibility
 (fn [db]
   (q/get-visibility db)))