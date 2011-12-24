(ns noir-async-chat.server
  (:use aleph.http
        noir.core
        lamina.core)
  (:require
    [noir.server :as nr-server] ))

(nr-server/load-views "src/noir_async_chat/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "3000"))
        noir-handler (nr-server/gen-handler {:mode mode})]
    (start-http-server
      (wrap-ring-handler noir-handler)
      {:port port :websocket true})))
