(ns thagomizer.components.core
  (:require
   [re-frame.core :as rf]
   [thagomizer.events.core :as events]
   [thagomizer.subs.core :as subs]))

(defn target-value [event]
  (.-value (.-target event)))

(defn on-value-change [e]
  (rf/dispatch [::events/update-text-field (target-value e)]))


(defn message [timestamp uid text]
  [:div {:id timestamp}
   [:span {:class "user"} uid]
   [:span {:class "text"} text]])

(defn text-field-field []
  (let [text-field (rf/subscribe [::subs/text-field])]
    [:form {:on-submit (fn [e]
                         (.preventDefault e)
                         (rf/dispatch [::events/submit-message]))}
     [:div [:input {:name :text-field
                    :value @text-field
                    :on-change #(on-value-change %)}]]]
))
