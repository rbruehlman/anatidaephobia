(ns thagomizer.utils)

(defn sleep [timeout]
  (let [maxtime (+ (.getTime (js/Date.)) timeout)]
    (while (< (.getTime (js/Date.)) maxtime))))