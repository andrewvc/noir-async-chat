(ns noir-async-chat.views.chatroom
  (:require [noir-async-chat.views.common :as common]
            [noir-async-chat.models.chatroom :as room]
            [cheshire.core :as json])
  (:use noir-async.core))

(defn msg-set-handle [conn handle]
  (cond
    (room/add-user handle)
      (async-push conn
        (json/generate-string {:mtype "handle-set-succ" :data handle}))
    :else
      (async-push conn
        (json/generate-string {:mtype "handle-set-fail" :data handle}))))
   
(defn msg-chat [handle text]
  (room/send-chat handle text))

(def ^:dynamic *conn-sess* nil)

(defn dispatch-message [conn-sess conn msg-raw]
  (let [{:strs [mtype data]} (json/parse-string msg-raw)]
    (cond
      (= mtype "set-handle")
        (do 
          (msg-set-handle conn data)
          (swap! conn-sess assoc :handle data))
      (= mtype "chat")
        (msg-chat (@conn-sess :handle) data)
      :else
        (println (str "Unrecognized command: " msg-raw)))))

(defpage-async "/room" {} conn
  (room/subscribe-channel (:request-channel conn))
  (let [conn-sess (atom {})]
    (on-close conn (fn []
      (room/remove-user (:handle @conn-sess))
      (println (str "Conn closed"))))
    (on-receive conn
      (fn [msg]
        (println "Recvd" msg)
        (dispatch-message conn-sess conn msg)
            (async-push conn msg)))))
