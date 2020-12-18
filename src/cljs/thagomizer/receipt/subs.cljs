(ns thagomizer.receipt.subs
  (:require
   [re-frame.core :as rf]
   [thagomizer.receipt.queries :as q]))

(rf/reg-sub
 ::messages
 (fn [db]
   (q/get-messages db)))

(rf/reg-sub
 ::page-count
 (fn [_]
   (rf/subscribe [::messages]))
 (fn [messages]
   (:page_count (first messages))))

(rf/reg-sub
 ::current-page
 (fn [_]
   (rf/subscribe [::messages]))
 (fn [messages]
   (:current_page (first messages))))