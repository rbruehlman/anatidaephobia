(ns thagomizer.entry.events.authentication
  (:require [re-frame.core :as rf]
            [clojure.string :as str]
            [thagomizer.common.funcs :as f-utils]
            [thagomizer.entry.queries.authentication :as auth-q]))


(rf/reg-event-db
 ::update-passcode-field
 (fn [db [_ value]]
   (auth-q/set-passcode-field db value)))

(defn invalid-password [passcode mode]
  (not (and (str/includes? passcode "m00m00")
            (f-utils/not-nil? mode))))

(rf/reg-event-db
 ::submit-passcode
 (fn [db]
   (let [passcode (auth-q/get-passcode-field db)
         mode-abbrev (str (second (str/split passcode "-")))
         modes {:c :chat :r :receipt :s :send}
         mode ((keyword mode-abbrev) modes)]

     (as-> db db
       (cond
         (invalid-password passcode mode)
         (-> db
             (auth-q/set-authentication :chat false)
             (auth-q/set-authentication :receipt false)
             (auth-q/set-authentication :send false))
         :else
         (auth-q/set-authentication db mode true))
       (if (str/includes? passcode "m00m00m00")
         (auth-q/set-admin-status db true)
         (auth-q/set-admin-status db false))))))