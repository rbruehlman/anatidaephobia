(ns thagomizer.chat.events.visibility
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.visibility :as visibility-q]
   [thagomizer.entry.queries.authentication :as auth-q]))

(def audio
  (let [sound (new js/Audio "audio/notification3.mp3")]
    (set! (.-crossOrigin sound) "anonymous")
    sound))

(rf/reg-event-db
 ::set-hidden-value
 (fn [db [_ value]]
   (visibility-q/set-hidden-value db value)))

(defn is-hidden? []
  (or (.-hidden js/document)
      (.-msHidden js/document)
      (.-webkitHidden js/document)))

(def visibility-type
  (cond
    (exists? (.-hidden js/document))  "visibilitychange"
    (exists? (.-msHidden js/document)) "msvisibilitychange"
    (exists? (.-webkitHidden js/document)) "webkitvisibilitychange"
    :else nil))

(defn handle-visibility-change []
  (rf/dispatch [::set-hidden-value (is-hidden?)]))

(defn set-visibility-listener []
   (.addEventListener js/document
                      visibility-type
                      handle-visibility-change
                      false))

(rf/reg-event-fx
 ::play-sound
 (fn [cofx]
  (let [db (:db cofx)
        hidden? (visibility-q/get-hidden-value db)
        admin? (auth-q/get-admin-status db)]
    (when (and hidden? admin?)
      (.play audio))
    {})))