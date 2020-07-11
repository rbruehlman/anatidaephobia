(ns thagomizer.components.core
  (:require
   [thagomizer.components.header :refer [header]]
   [thagomizer.components.input :refer [input-text-field]]
   [thagomizer.components.messages :refer [messages]]
   [thagomizer.components.typing :refer [typing-indicator]]
   [thagomizer.components.utils :as c-utils]))

(defn app []
  [:div#flex-container
   {:style (merge {:width "70%"} c-utils/center-css)}
   [header]
   [messages]
   [:div
    [typing-indicator]]
   [:div
    [input-text-field]]])