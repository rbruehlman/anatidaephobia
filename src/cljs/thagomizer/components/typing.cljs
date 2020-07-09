(ns thagomizer.components.typing
  (:require
   [re-frame.core :as rf]
   [thagomizer.components.utils :as c-utils]
   [thagomizer.subs.core :as subs]
   [clojure.string :refer [join]]))

(defn is-typing-text [users-typing]
  (let [user-count (count users-typing)]
    (str (cond
           (>= user-count 3)
           (str (join ", "
                      (update users-typing
                              (- user-count 1)
                              #(str "and " %))) " are typing...")

           (= user-count 2)
           (str (join " and " users-typing) " are typing...")

           :else ;;technically i guess no one could be typing ¯ \_(ツ)_/¯
           (str (join " and " users-typing) " is typing...")))))

(defn typing-indicator []
  (let [users-typing @(rf/subscribe [::subs/typing-status])]
    (if (not (empty? users-typing))
      [:div
       [:div#typing-status
        {:style (merge {:font-family "Roboto, sans-serif"
                        :font-style "italic"} c-utils/center-css)}
        (is-typing-text users-typing)]]
      [:div
       [:div#typing-status
        {:style (merge {:visibility "hidden"} c-utils/center-css)}
        "No one is typing, so this is hidden :("]])))