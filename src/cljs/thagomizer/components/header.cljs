(ns thagomizer.components.header
  (:require
   [thagomizer.components.utils :as c-utils]))

(defn header []
  [:h1
   {:style (merge {:font-family "Gloria Hallelujah, cursive"
                   :font-size "3vw"
                   :display "flex"
                   :padding-bottom "50px"}
                  c-utils/center-css)}
   "THE THAGOMIZER"])