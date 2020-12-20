(ns thagomizer.common.funcs
  (:require [cljsjs.moment]))

(defn sleep [timeout]
  (let [maxtime (+ (.getTime (js/Date.)) timeout)]
    (while (< (.getTime (js/Date.)) maxtime))))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn not-in?
  "true if coll does not contain elm"
  [coll elm]
  (not (in? coll elm)))

(defn dissoc-in
  "removes from a nested map"
  [_map keys to-remove]
  (update-in _map keys dissoc to-remove))

(defn convert-to-human-time
  "Display time in h:mm A format, e.g. 10:23 PM.
   The try/catch here is because there were invalid dates for some reason?"
  [unix-time format]
  (try
    (.format (js/moment unix-time) format)
    (catch :default e
      e)))

(defn not-nil? [val]
  (not (nil? val)))

(defn not-empty? [val]
  (comp (empty? val)))

(defn queue
  "Make things into a queue"
  ([coll]
   (reduce conj #queue [] coll)))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))
