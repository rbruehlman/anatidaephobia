(ns thagomizer.ws.utils
  (:require
   [taoensso.encore :as encore]))

(def output-el (.getElementById js/document "output"))

(defn ->output! [fmt & args]
  (let [msg (apply encore/format fmt args)]
    (aset output-el "value" (str "• " (.-value output-el) "\n" msg))
    (aset output-el "scrollTop" (.-scrollHeight output-el))))