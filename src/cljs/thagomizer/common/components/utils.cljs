(ns thagomizer.common.components.utils
  (:require [clojure.string :as str]))

(def center-css {:margin "auto"
                 :justify-content "center"})

(def get-media-type (.-type (.-styleMedia js/window)))

(defn get-client-rect [this]
  (let [r (.getBoundingClientRect this)] 
    {:left (.-left r)
     :top (.-top r)
     :right (.-right r)
     :bottom (.-bottom r)
     :width (.-width r)
     :height (.-height r)}))

(defn update-dimensions [state dom-rect]
  
  (swap! state assoc
         :top (.-top dom-rect)
         ;:bottom (.-bottom dom-rect)
         :left (.-left dom-rect)
         ;:right (.-right dom-rect)
         :max-height (.-height dom-rect)
         :max-width (.-width dom-rect)))

(defn trunc-uid [uid]
  (first (str/split (name uid) #"-")))

(defn split-paragraph [msg]
  (str/split msg "\n"))