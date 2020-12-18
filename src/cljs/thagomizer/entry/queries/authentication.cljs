(ns thagomizer.entry.queries.authentication)

(defn set-authentication [db mode bool]
  (assoc-in db [mode :authenticated] bool))

(defn get-authentication [db mode]
  (get-in db [mode :authenticated]))

(defn set-passcode-field [db value]
  (assoc db :passcode value))

(defn get-passcode-field [db]
  (get db :passcode))