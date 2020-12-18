(ns thagomizer.chat.subs.messages
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.funcs :as f-utils]
   [thagomizer.chat.queries.messages :as message-q]
   [thagomizer.chat.queries.uids :as uid-q]))

(rf/reg-sub
 ::newest-message
 (fn [db]
   (message-q/get-newest-message db)))

(defn- get-author-color [msg uids]
  ((keyword (message-q/get-message-author msg)) uids))

;;Gets messages and assigns color to them based on author
(rf/reg-sub
 ::latest-messages
 (fn [db]
   (let [messages (message-q/get-messages db)
         uids     (uid-q/get-uids db)]
     (seq (for [m messages]
            (do
              (when (nil? (get-author-color m uids))
                (f-utils/sleep 10))
              (message-q/set-message-color m (get-author-color m uids))))))))