(ns thagomizer.ws.client
  (:require
   [taoensso.sente  :as sente]))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(let [packer :edn
    {:keys [chsk ch-recv send-fn state]}
    (sente/make-channel-socket! "/chsk" ?csrf-token {:type :auto
                                                     :packer packer})]

(def chsk       chsk)
(def ch-chsk    ch-recv) ; ChannelSocket's receive channel
(def chsk-send! send-fn) ; ChannelSocket's send API fn
(def chsk-state state)   ; Watchable, read-only atom
)