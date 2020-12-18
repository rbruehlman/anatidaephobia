(ns thagomizer.chat.subs.uids
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.uids :as uid-q]))

(rf/reg-sub
 ::uid
 (fn [db]
   (uid-q/get-self-uid db)))

(rf/reg-sub
 ::uids
 (fn [db]
   (uid-q/get-uids db)))