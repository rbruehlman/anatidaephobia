(ns thagomizer.components.utils
  (:require [clojure.string :as str]))

(def center-css {:margin "auto"
                 :justify-content "center"})

(def get-media-type (.-type (.-styleMedia js/window)))

(defn cs
  "Multiple classes!"
  [& args]
  (str/join " " (map name (filter identity args))))

(defn trunc-uid [uid]
  (first (str/split uid #"-")))

(defn split-paragraph [msg]
  (str/split msg "\n"))