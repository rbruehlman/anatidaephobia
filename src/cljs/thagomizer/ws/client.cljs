(ns thagomizer.ws.client
  (:require
   [taoensso.sente  :as sente]
   [thagomizer.subs.core :as subs]
   [re-frame.core :as rf]
   [thagomizer.events.uids :as uid-events]))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(defn get-uid []
  @(rf/subscribe [::subs/uid]))

(rf/dispatch [::uid-events/get-adjective])

(let [packer :edn
    {:keys [chsk ch-recv send-fn state]}
    (sente/make-channel-socket! "/chsk" ?csrf-token {:type :auto
                                                      :packer packer
                                                      :client-id get-uid})]

(def chsk       chsk)
(def ch-chsk    ch-recv) ; ChannelSocket's receive channel
(def chsk-send! send-fn) ; ChannelSocket's send API fn
(def chsk-state state)   ; Watchable, read-only atom
)