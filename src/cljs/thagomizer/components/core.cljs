(ns thagomizer.components.core
  (:require
   [thagomizer.components.header :refer [header]]
   [thagomizer.components.input :refer [input-text-field]]
   [thagomizer.components.messages :refer [messages]]
   [thagomizer.components.typing :refer [typing-indicator]]))

(defn app []
  [:div#flex-container
   [header]
   [:div
    [typing-indicator]]
   [:div
    [input-text-field]]
   [messages]])