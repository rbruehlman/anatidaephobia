(ns thagomizer.events.uids
  (:require
   [re-frame.core :as rf]
   [clojure.set :as set]))

(defn assign-color [uids]
  (let [colors #{"blue" "red" "green" "orange" "purple"}
        taken-colors (set (vals uids))
        available-colors (set/difference colors taken-colors)]
    (if (= (count available-colors) 0) ;; all available colors taken; start over
      (first colors)
      (first available-colors))))

(defn add-uid-and-color
  [db uid]
  (let [existing-uids  (:uids db)
        color (assign-color existing-uids)]
    (assoc-in db [:uids (keyword uid)] color)))

(defn add-uids [db vec]
  (let [[uid & remaining-uids] vec]
    (if (empty? remaining-uids)
      (add-uid-and-color db uid)
      (-> db
          (add-uid-and-color uid)
          (add-uids remaining-uids)))))

(defn remove-uids
  [db uids-to-remove]
  (let [existing-uids (:uids db)]
    (assoc db :uids
           (apply dissoc existing-uids uids-to-remove))))

(rf/reg-event-db
 ::set-uids
 (fn [db [_ msg]]
   (let [active-uids (set (:uids msg))
         current-uids (if (empty? (:uids db))
                        {}
                        (set (keys (:uids db))))
         inactive-uids (set/difference current-uids active-uids)
         new-uids (set/difference active-uids current-uids)]
     (-> db
         (remove-uids inactive-uids)
         (add-uids new-uids)))))
