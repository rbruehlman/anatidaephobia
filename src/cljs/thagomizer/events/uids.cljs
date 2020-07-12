(ns thagomizer.events.uids
  (:require
   [re-frame.core :as rf]
   [clojure.set :as set]
   [ajax.core :as ajax]))

(defn assign-color [uids]
  (let [colors #{"blue" "red" "green" "orange" "purple"}
        taken-colors (set (vals uids))
        available-colors (set/difference colors taken-colors)]
    (if (= (count available-colors) 0) ;; all available colors taken; start over
      (first colors)
      (first available-colors))))

(defn add-uid-and-color
  [db uid]
  (let [existing-uids  (:uids db)
        color (assign-color existing-uids)]
    (assoc-in db [:uids (keyword uid)] color)))

(defn add-uids [db vec]
  (let [[uid & remaining-uids] vec]
    (if (empty? remaining-uids)
      (add-uid-and-color db uid)
      (-> db
          (add-uid-and-color uid)
          (add-uids remaining-uids)))))

(defn remove-uids
  [db uids-to-remove]
  (let [existing-uids (:uids db)]
    (assoc db :uids
           (apply dissoc existing-uids uids-to-remove))))

(rf/reg-event-db
 ::set-uids
 (fn [db [_ msg]]
   (let [active-uids (set (:uids msg))
         current-uids (if (empty? (:uids db))
                        {}
                        (set (keys (:uids db))))
         inactive-uids (set/difference current-uids active-uids)
         new-uids (set/difference active-uids current-uids)]
     (-> db
         (remove-uids inactive-uids)
         (add-uids new-uids)))))

(rf/reg-event-db
 ::add-self-uid
 (fn [db [_ uids]]
   (assoc db :uid uids)))


(defn build-specs [pos]
  {:method :get
   :uri "https://wordsapiv1.p.rapidapi.com/words/"
   :format (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :params {:random true
            :limit 1
            :partOfSpeech pos
            :frequencyMin 2.5
            :letterPattern "^[A-z]+$"
            :apiKey "36067c882dmsh6a679e610ad6906p1252e5jsnb3db23d7a4b3"}
   :headers {:x-rapidapi-host "wordsapiv1.p.rapidapi.com"
             :x-rapidapi-key "36067c882dmsh6a679e610ad6906p1252e5jsnb3db23d7a4b3"}})

(rf/reg-event-db
 ::assign-dummy-uid
 (fn [db]
   (assoc db :uid (str (random-uuid)))))

(rf/reg-event-db
 ::create-uid
 (fn [db [_ adjective {noun :word}]]
   (assoc db :uid (str adjective "-" noun))))

(rf/reg-event-fx
 ::get-adjective
 (fn []
   (let [specs (build-specs "adjective")]
     {:http-xhrio (merge specs
                         {:on-success [::get-noun]
                          :on-failure [::assign-dummy-uid]})})))

(rf/reg-event-fx
 ::get-noun
 (fn [{event :event}]
   (let [specs (build-specs "noun")
         adjective  (:word (last event))]
     {:http-xhrio (merge specs
                         {:on-success [::create-uid adjective]
                          :on-failure [::assign-dummy-uid]})})))