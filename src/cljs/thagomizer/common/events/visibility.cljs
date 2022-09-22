(ns thagomizer.common.events.visibility
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.queries.visibility :as visibility-q]
   [thagomizer.entry.queries.authentication :as auth-q]
   [thagomizer.common.funcs :as f-utils]))


(rf/reg-event-db
 ::set-hidden-value
 (fn [db [_ value]]
   (visibility-q/set-hidden-value db value)))

(defn is-hidden? []
  (or (.-hidden js/document)
      (.-msHidden js/document)
      (.-webkitHidden js/document)))

(defn visibility-type []
  (cond
    (exists? (.-hidden js/document))  "visibilitychange"
    (exists? (.-msHidden js/document)) "msvisibilitychange"
    (exists? (.-webkitHidden js/document)) "webkitvisibilitychange"
    :else nil))

(defn is-mobile []
  (f-utils/not-nil? (.match (.-userAgent js/navigator) #"(?i)iPhone")))

(defn handle-visibility-change []
  (rf/dispatch [::set-hidden-value (is-hidden?)]))

(rf/reg-event-fx
 ::quit-if-visible
 (fn [cofx]
   (let [db (:db cofx)
         admin? (auth-q/get-admin-status db)]
     (.log js/console (and
                       (is-hidden?)
                       (not admin?)
                       (not is-mobile)))
     (when (and
            (is-hidden?)
            (not admin?)
            (is-mobile))
       (.close js/window))
     {})))


(defn handle-visibility-quit []
  (rf/dispatch [::quit-if-visible]))


(defn set-visibility-listener []
  (.addEventListener js/document
                     (visibility-type)
                     #((handle-visibility-change)
                       (handle-visibility-quit)
                       false)))

(defn remove-visibility-listener []
  (.removeEventListener js/document
                     (visibility-type)
                     #((handle-visibility-change)
                       (handle-visibility-quit)
                       false)))
