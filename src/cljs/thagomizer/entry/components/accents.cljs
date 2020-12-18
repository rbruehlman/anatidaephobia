(ns thagomizer.entry.components.accents
  (:require [thagomizer.common.components.utils :as c-utils]))

(defn header []
  [:h1
   {:style (merge {:font-family "Gloria Hallelujah, cursive"
                   :font-size "4vmax"
                   :display "flex"
                   :padding "20px 0 20px 0px"
                   :text-align "center"}
                  c-utils/center-css)}
   "THE THAGOMIZER"])