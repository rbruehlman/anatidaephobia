(ns thagomizer.utils)

(defn sleep [timeout]
  (let [maxtime (+ (.getTime (js/Date.)) timeout)]
    (while (< (.getTime (js/Date.)) maxtime))))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))