(ns thagomizer.chat.events.visibility
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.visibility :as visibility-q]
   [thagomizer.entry.queries.authentication :as auth-q]))

(def audio
  (let [sound (new js/Audio "audio/notification3.mp3")]
    (set! (.-crossOrigin sound) "anonymous")
    sound))


(rf/reg-event-fx
 ::play-sound
 (fn [cofx]
  (let [db (:db cofx)
        hidden? (visibility-q/get-hidden-value db)
        admin? (auth-q/get-admin-status db)]
    (when (and hidden? admin?)
      (.play audio))
    {})))
