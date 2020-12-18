(ns thagomizer.chat.subs.camera
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera :as camera-q]))

(rf/reg-sub
 ::camera-modal
 (fn [db]
   (camera-q/get-camera-modal db)))

(rf/reg-sub
 ::stream
 (fn [db]
   (camera-q/get-stream db)))

(rf/reg-sub
 ::photo
 (fn [db]
   (camera-q/get-active-photo db)))

(rf/reg-sub
 ::stream-loading-status
 (fn [db]
   (camera-q/get-stream-loading-status db)))

(rf/reg-sub
 ::photo-loading-status
 (fn [db]
   (camera-q/get-photo-loading-status db)))
