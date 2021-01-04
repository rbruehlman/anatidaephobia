(ns thagomizer.chat.events.utils
  (:require
   [re-frame.core :as rf]))


(defn timeout [event time]
  (js/setTimeout
   (fn []
     (rf/dispatch event))
   time))

(rf/reg-fx
 ::timeout-fx
 (fn [args]
   (if (= (type args) map)
     (timeout (:event args) (:timeout args))
     (doseq [event args]
       (timeout (:event event) (:timeout event))))))
