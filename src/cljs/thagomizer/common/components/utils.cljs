(ns thagomizer.common.components.utils
  (:require [clojure.string :as str]))

(def center-css {:margin "auto"
                 :justify-content "center"})

(def get-media-type (.-type (.-styleMedia js/window)))

(defn update-dimensions [state dom-rect]
  (swap! state assoc
         :top (.-top dom-rect)
         :left (.-top dom-rect))
  :height (.-top dom-rect)
  :width (.-top dom-rect))

(defn trunc-uid [uid]
  (first (str/split (name uid) #"-")))

(defn split-paragraph [msg]
  (str/split msg "\n"))