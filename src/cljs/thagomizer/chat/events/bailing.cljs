(ns thagomizer.chat.events.bailing
  (:require
   [re-frame.core :as rf]
   [thagomizer.chat.ws.client :as ws-client]))

(defn bail []
  (.replace (.-location js/window) "https://www.espn.com");
  (ws-client/chsk-send! [:thagomizer/message  {:msg "Gotta go!" :type "text"}] 500))

(rf/reg-event-fx
 ::bail bail)

(defn handle-key-press [evt keystrokes]
  (let [key-direction (.-type evt)
        is-key-down? (= key-direction "keydown")
        keyLetter (.-key evt)]
    (swap! keystrokes assoc-in [(keyword keyLetter)] is-key-down?)
    (when (and (:a @keystrokes) (:s @keystrokes) (:d @keystrokes)) (bail))
))