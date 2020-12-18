(ns thagomizer.queries
  (:require [thagomizer.db :refer [ds]]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

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
  (jdbc/execute! ds [(format "INSERT INTO visit (timestamp)
                              VALUES (DEFAULT);")]))

(defn get-next-prompt []
  (first
    (jdbc/execute! ds ["SELECT
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
                             GROUP BY prompt_id
                             ORDER BY MAX(timestamp) ASC) last
                          ON prompt.id = last.prompt_id) as prompts
                        ORDER BY last_used ASC
                        LIMIT 1"]
                   {:builder-fn rs/as-unqualified-maps})))

(defn create-message-table []
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS message (
                      id    SERIAL PRIMARY KEY,
                      message    TEXT NOT NULL,
                      timestamp    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      prompt_id    INT REFERENCES prompt(id)
                      );"]))

(defn insert-message [message prompt_id]
  (jdbc/execute! ds ["INSERT INTO message (message, prompt_id)
                      VALUES (?, ?);"
                     message prompt_id]))

(defn get-messages [offset]
  (jdbc/execute! ds [(str "SELECT
                      message.id,
                      message.timestamp,
                      message.message,
                      CASE WHEN last_visit.rank IS NOT NULL
                           THEN TRUE
                           ELSE FALSE
                           END AS new
                      FROM message
                      LEFT JOIN (
                         SELECT timestamp,
                                rank
                         FROM (SELECT timestamp, ROW_NUMBER() OVER 
                                      (ORDER BY timestamp DESC) as rank
                               FROM visit) recency
                         WHERE rank = 2) last_visit
                      ON message.timestamp >= last_visit.timestamp
                      ORDER BY message.timestamp DESC
                      LIMIT 10 
                      OFFSET " offset)]
                     {:builder-fn rs/as-unqualified-maps}))

(defn get-page-count []
  (first
   (jdbc/execute! ds ["SELECT
                          FLOOR(COUNT(*)/10.0) as pg_ct
                       FROM message"]
                  {:builder-fn rs/as-unqualified-maps})))
