(ns thagomizer.chat.subs.camera.photo
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.photo :as photo-q]))

(rf/reg-sub
 ::photo-data
 (fn [db]
   (photo-q/get-photo-data db)))

(rf/reg-sub
 ::photo-visibility
 (fn [_]
   (rf/subscribe [::photo-data]))
 (fn [url]
   (not (nil? url))))

(rf/reg-sub
 ::photo-loading-status
 (fn [db]
   (photo-q/get-photo-loading-status db)))