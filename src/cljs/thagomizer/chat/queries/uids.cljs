(ns thagomizer.chat.queries.uids
  (:require [thagomizer.common.funcs :as f-utils]))

(defn set-self-uid [db uid]
  (assoc-in db [:chat :uid] uid))

(defn set-uid-color
  [db uid color]
  (assoc-in db [:chat :uids (keyword uid)] color))

(defn remove-uid
  [db uid]
  (f-utils/dissoc-in db [:chat :uids] (keyword uid)))

(defn get-self-uid [db]
  (get-in db [:chat :uid]))

(defn get-uids [db]
  (get-in db [:chat :uids]))
