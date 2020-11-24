(ns thagomizer.events.uids
  (:require
   [re-frame.core :as rf]
   [clojure.set :as set]))

(defn assign-color
  "Assigns color based off available colors.  If all are
  assigned, take one of the least used."
  [uids]
  (let [colors #{"blue" "red" "green" "orange" "purple"}
        taken-colors (vals uids)
        available-colors (set/difference colors (set taken-colors))]
    (if (= (count available-colors) 0)
      (first (apply min-key val (frequencies taken-colors)))
      (first available-colors))))

(defn add-uid-and-color
  "Adds a map of uid and color to the uids map"
  [db uid]
  (let [existing-uids  (:uids db)
        color (assign-color existing-uids)]
    (assoc-in db [:uids (keyword uid)] color)))

(defn add-uids
  "Recursively adds uids and color.
   Why did I make this recursive again???  I *think* I needed to
   keep track of what colors had already been used in the event multiple
   uids needed to be added at once? (as opposed to) adding sequentially?
   Or something?  I'm not sure I HAD to do it this way now, but whatever,
   it works."
  [db vec]
  (let [[uid & remaining-uids] vec]
    (if (empty? remaining-uids)
      (add-uid-and-color db uid)
      (-> db
          (add-uid-and-color uid)
          (add-uids remaining-uids)))))

(defn remove-uids
  "Remove all inactive uids"
  [db uids-to-remove]
  (let [existing-uids (:uids db)]
    (assoc db :uids
           (apply dissoc existing-uids uids-to-remove))))

;; Set uids (removing and adding as necessary)
;; I wonder if maybe I should make add/remove events when it receives an arrival/departure message
;; from the websocket? hmm.
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

(rf/reg-event-db
 ::set-self-uid
 (fn [db [_ msg]]
   (assoc db :uid msg)))
