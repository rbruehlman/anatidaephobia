(ns thagomizer.utils)

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