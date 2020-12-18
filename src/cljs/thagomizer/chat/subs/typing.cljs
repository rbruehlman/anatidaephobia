(ns thagomizer.chat.subs.typing
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.typing :as typing-q]
   ))

(rf/reg-sub
 ::typing-status
 (fn [db]
   (typing-q/get-others-typing-status db)))
