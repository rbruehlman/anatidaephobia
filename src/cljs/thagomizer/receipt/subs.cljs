(ns thagomizer.receipt.subs
  (:require
   [re-frame.core :as rf]
   [thagomizer.receipt.queries :as q]))

(rf/reg-sub
 ::messages
 (fn [db]
   (q/get-messages db)))