(ns thagomizer.entry.subs.authentication
  (:require
   [re-frame.core :as rf]
   [thagomizer.entry.queries.authentication :as auth-q]))

(rf/reg-sub
 ::passcode
 (fn [db]
   (auth-q/get-passcode-field db)))

(rf/reg-sub
 ::authorized-mode
 (fn [db [_ mode]]
   (auth-q/get-authentication db mode)))

(rf/reg-sub
 ::authentication
 (fn [_]
   (vec (map #(rf/subscribe [::authorized-mode %]) [:chat :receipt :send])))
 (fn [[chat receipt send]]
   (or chat receipt send)))