(ns thagomizer.db.queries
  (:require [thagomizer.db.connection :refer [ds]]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.date-time]
            ))

(defn create-prompt-table []
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS prompt (
                      id SERIAL PRIMARY KEY,
                      prompt TEXT NOT NULL,
                      UNIQUE(prompt)
                      );"]))

(defn insert-prompt [prompt]
  (jdbc/execute! ds ["INSERT INTO prompt (prompt)
                      VALUES (?);"
                     prompt]))

(defn create-visit-table []
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS visit (
                      id    SERIAL PRIMARY KEY,
                      timestamp    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                      );"]))

(defn insert-visit []
  (jdbc/execute! ds ["INSERT INTO visit (timestamp)
                     VALUES (DEFAULT);"]))

(defn- get-prompt [order]
  (jdbc/execute-one! ds [(str 
                          "SELECT
                          id,
                          prompt,
                          last_used
                       FROM 
                          (SELECT
                             prompt.id,
                             prompt.prompt,
                             CASE WHEN last_used IS NULL
                               THEN make_timestamptz(1970, 1, 1, 12, 0, 0)
                               ELSE last_used
                               END as last_used
                          FROM prompt
                          LEFT JOIN (
                             SELECT
                                prompt_id,
                                MAX(timestamp) as last_used
                             FROM message
                           WHERE message.admin IS TRUE
                               OR message.admin IS NULL
                             GROUP BY prompt_id
                             ORDER BY MAX(timestamp) ASC) last
                          ON prompt.id = last.prompt_id) as prompts
                        ORDER BY last_used " order
                        " LIMIT 1")]
                   {:builder-fn rs/as-unqualified-maps}))

(defn get-last-prompt []
  (get-prompt "DESC"))

(defn get-next-prompt []
  (get-prompt "ASC"))

(defn create-message-table []
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS message (
                      id    SERIAL PRIMARY KEY,
                      message    TEXT NOT NULL,
                      timestamp    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      admin    BOOL,
                      prompt_id    INT REFERENCES prompt(id)
                      );"]))

(defn insert-message [message prompt_id admin]
  (jdbc/execute! ds ["INSERT INTO message (message, prompt_id, admin)
                      VALUES (?, ?, ?);"
                     message prompt_id admin]))

(defn insert-image [image prompt_id]
  (jdbc/execute! ds ["INSERT INTO message (image, prompt_id)
                      VALUES (?, ?);"
                     image prompt_id]))

(defn get-last-message-timestamp []
  (:recent
   (jdbc/execute-one! ds ["SELECT
                           (MAX(timestamp::DATE) + INTERVAL '3 hours') > NOW()::DATE as recent
                           FROM message
                           WHERE admin IS TRUE
                             OR admin IS NULL"])
   {:builder-fn rs/as-unqualified-maps}))

(defn get-messages [page]
  (let [offset (* (- page 1) 10)]
  (jdbc/execute! ds ["SELECT
                      message.id,
                      message.timestamp,
                      message.message,
                      CEIL(COUNT(*) OVER () / 10.0) as page_count,
                      (?::int+10)/10 as current_page,
                      CASE WHEN last_visit.dummy IS NOT NULL
                           THEN TRUE
                           ELSE FALSE
                           END AS new
                      FROM message
                      LEFT JOIN (
                         SELECT
                           1 as dummy,
                           max(timestamp) as timestamp
                         FROM visit) last_visit
                      ON message.timestamp >= last_visit.timestamp
                      WHERE message.admin IS TRUE
                         OR message.admin IS NULL
                      ORDER BY message.timestamp DESC
                      LIMIT 10
                      OFFSET ?::int" offset offset]
                     {:builder-fn rs/as-unqualified-maps})))

(defn get-page-count []
  (jdbc/execute-one! ds ["SELECT
                          FLOOR(COUNT(*)/10.0) as pg_ct
                       FROM message"]
                  {:builder-fn rs/as-unqualified-maps}))

(defn get-visits []
  (jdbc/execute! ds ["SELECT
                      *
                      FROM visit
                      ORDER BY timestamp DESC"]))