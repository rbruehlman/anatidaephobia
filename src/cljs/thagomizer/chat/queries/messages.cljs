(ns thagomizer.chat.queries.messages
  (:require [thagomizer.common.funcs :as f-utils]))

(defn get-messages [db]
  (get-in db [:chat :messages]))

(defn get-newest-message [db]
  (get-in db [:chat :newest-message]))

(defn replace-message [db new-message]
  (update-in db [:chat :messages]
             #(conj (pop %) new-message)))

(defn add-message [db new-message]
  (update-in db [:chat :messages]
             conj new-message))

(defn update-former-uid-msgs [db lost-uid messages]
  (assoc-in db [:chat :messages]
            (f-utils/queue (map #(if (= (:author %) lost-uid)
                                   (assoc % :author "lost-uid")
                                   %)
                                messages))))

(defn remove-former-uid-msgs [db lost-uid messages]
  (assoc-in db [:chat :messages]
            (f-utils/queue (filter
                            #(not= lost-uid (keyword (:author %)))
                            messages))))

(defn get-message-author [msg]
  (get-in msg [:author]))

(defn set-message-color [msg color]
  (assoc-in msg [:color] color))
