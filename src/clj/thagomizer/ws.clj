(ns thagomizer.ws
  (:require
   [ring.middleware.defaults]
   [taoensso.sente :as sente]
   [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
   [thagomizer.utils :as utils]))

;;;; Define our Sente channel socket (chsk) server	
(let [packer :edn
      chsk-server
      (sente/make-channel-socket!
       (get-sch-adapter) {:packer packer :user-id-fn utils/ip-address})
      {:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      chsk-server]
  
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)                                  ; ChannelSocket's receive channel	
  (def chsk-send! send-fn)                               ; ChannelSocket's send API fn	
  (def connected-uids connected-uids)                    ; Watchable, read-only atom	
  )
