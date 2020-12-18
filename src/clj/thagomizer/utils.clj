(ns thagomizer.utils
  (:require [cheshire.core :refer [parse-stream]]
            [clojure.java.io :as io]))

(defn json-to-map [ring-req]
  (parse-stream (io/reader (:body ring-req) :encoding "UTF-8") true))