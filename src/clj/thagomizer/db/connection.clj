(ns thagomizer.db.connection
  (:require [next.jdbc :as jdbc]))

(def db {:dbtype "postgres"
         :dbname "postgres"
         :user "postgres"
         :password (System/getenv "DB_PASSWORD")
         :host (System/getenv "DB_HOST")})

(def ds (jdbc/get-datasource db))
