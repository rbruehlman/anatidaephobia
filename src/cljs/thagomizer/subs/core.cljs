(ns thagomizer.subs.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.utils :as utils]))

(rf/reg-sub
 ::text-field
 (fn [db]
   (:text-field db)))

(rf/reg-sub
 ::newest-message
 (fn [db]
   (:newest-message db)))

(rf/reg-sub
 ::typing-status
 (fn [db]
   (get-in db [:is-typing :others])))

(rf/reg-sub
 ::latest-messages
 (fn [db]
   (seq (:messages db))))

(rf/reg-sub
 ::uid
 (fn [db]
   (:uid db)))

(defn- get-user-color [msg uids]
  ((keyword (:author msg)) uids))

(rf/reg-sub
 ::latest-messages
 (fn [db]
   (let [messages (:messages db)
         uids     (:uids db)]
     (seq (for [m messages]
            (if (nil? (get-user-color m uids))
              (utils/sleep 10)
            (assoc m :color (get-user-color m uids))))))))