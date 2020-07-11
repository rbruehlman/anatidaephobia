(ns thagomizer.components.utils
  (:require [clojure.string :as str]))

(def center-css {:margin "auto"
                 :justify-content "center"
                 :align-items "center"})

(defn get-media-type []
  (.-type (.-styleMedia js/window)))

(defn cs [& args]
  (str/join " " (map name (filter identity args))))