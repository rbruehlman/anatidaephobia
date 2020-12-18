(ns thagomizer.chat.subs.input
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.input :as input-q]))

(rf/reg-sub
 ::text-field
 (fn [db]
   (input-q/get-text-field db)))