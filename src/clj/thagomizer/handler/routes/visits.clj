(ns thagomizer.handler.routes.visits
  (:require [thagomizer.db.queries :as q]))

(defn visit-handler [ring-req]
  (q/insert-visit)
  {:status  201})