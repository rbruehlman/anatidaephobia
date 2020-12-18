(ns thagomizer.chat.events.uids
  (:require
   [re-frame.core :as rf]
   [clojure.set :as set]
   [thagomizer.chat.queries.uids :as uid-q]))

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
  (let [existing-uids  (uid-q/get-uids db)
        color (assign-color existing-uids)]
    (uid-q/set-uid-color db uid color)))

(defn- set-uids
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
          (set-uids remaining-uids)))))

(rf/reg-event-db
 ::set-uids
 (fn [db [_ {:keys [uids]}]]
   (set-uids db uids)))

(rf/reg-event-db
 ::add-uid
 (fn [db [_ uid]]
   (let [existing-uids  (uid-q/get-uids db)
         color (assign-color existing-uids)]
     (uid-q/set-uid-color db uid color))))

(rf/reg-event-db
 ::remove-uid
 (fn [db [_ uid]]
   (uid-q/remove-uid db uid)))

(rf/reg-event-db
 ::set-self-uid
 (fn [db [_ ws-data]]
   (let [uid (:self-uid ws-data)]
     (uid-q/set-self-uid db uid))))
