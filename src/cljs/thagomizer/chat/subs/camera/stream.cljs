(ns thagomizer.chat.subs.camera.stream
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.camera.stream :as stream-q]))

(rf/reg-sub
 ::stream-visibility
 (fn [db]
   (stream-q/get-stream-visibility db)))

(rf/reg-sub
 ::stream-element
 (fn [db]
   (stream-q/get-stream-element db)))

(rf/reg-sub
 ::stream-loading-status
 (fn [db]
   (stream-q/get-stream-loading-status db)))

(rf/reg-sub
 ::stream-object
 (fn [db]
   (stream-q/get-stream-object db)))

;; timer
;; 
(rf/reg-sub
 ::timer
 (fn [db]
   (stream-q/get-timer db)))
