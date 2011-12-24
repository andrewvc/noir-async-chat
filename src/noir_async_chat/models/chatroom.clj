(ns noir-async-chat.models.chatroom
  (:require
    [cheshire.core :as json])
  (:use lamina.core
        noir-async.utils))

(def handles (ref #{}))

(defn has-user? [handle]
  (dosync
    (get (ensure handles) handle)))

(defn add-user [handle]
  (dosync
      (if (has-user? handle)
        false
        (alter handles conj handle))))
 
(defn remove-user [handle]
  (dosync
    (alter handles disj handle)))

(defn current-users []
  @handles)

(defn user-count []
  (count @handles))

(def chat-channel (permanent-channel))

(receive-all chat-channel #(println (str "CH> " %1)))

(defn broadcast-handles []
  (enqueue chat-channel
    (json/generate-string
      {:mtype "all-handles"
       :data @handles})))

(set-interval 1000 broadcast-handles)

(defn send-chat [handle message]
  (enqueue chat-channel
    (json/generate-string
      {:mtype "chat"
       :data  {:handle handle :text message}})))

(defn subscribe-channel [ch]
  (siphon chat-channel ch))
