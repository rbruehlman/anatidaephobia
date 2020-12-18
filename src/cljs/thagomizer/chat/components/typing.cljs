(ns thagomizer.chat.components.typing
  (:require
   [re-frame.core :as rf]
   [thagomizer.common.components.utils :as c-utils]
   [thagomizer.chat.subs.typing :as typing-subs]
   [clojure.string :refer [join]]))

(defn is-typing-text
  "Set the typing status text to reflect if one or more people are typing"
  [uids-typing]
  (let [uid-count (count uids-typing)]
    (str (cond
           (>= uid-count 3)
           (str (join ", "
                      (update uids-typing
                              (- uid-count 1)
                              #(str "and " %))) " are typing...")

           (= uid-count 2)
           (str (join " and " uids-typing) " are typing...")

           :else ;;technically i guess no one could be typing ¯ \_(ツ)_/¯
           (str (join " and " uids-typing) " is typing...")))))

(defn typing-indicator
  "Component for displaying who is typing"
  []
  (let [uids-typing @(rf/subscribe [::typing-subs/typing-status])]
    (if (not (empty? uids-typing))
      [:div
       [:div#typing-status
        {:style (merge {:font-family "Roboto, sans-serif"
                        :font-style "italic"
                        :display "flex"
                        :width "100%"}
                       c-utils/center-css)}
        (is-typing-text (map c-utils/trunc-uid uids-typing))]]
      [:div
       [:div#typing-status
        {:style (merge {:visibility "hidden"
                        :display "flex"}
                       c-utils/center-css)}
        "No one is typing, so this is hidden :("]])))