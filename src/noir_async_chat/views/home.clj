(ns noir-async-chat.views.home
  (:require [noir-async-chat.views.common :as common]
            [noir-async-chat.models.chatroom :as room])
  (:use [noir.core :only [defpage]]
        hiccup.form))

(defpage "/" {}
    (common/layout 
      [:div {:id "about"}
        "An example of creating synchronous / asynchronous apps
         using noir-async."
      ]
      [:div {:id "chatroom"}
        [:div {:id "chatroom-messages"}]
        [:div {:id "chatroom-controls" :style "display: none"}
          [:div {:id "chatroom-last-error"}]
          (form-to  {:id "set-handle-form"} [:post "/set-handle"]
            (label "handle" "Pick a handle: ")
            (text-field "handle" "")
            (submit-button "Join now"))
          (form-to  {:id "chat-message-form" :style "display: none"} [:post "/set-handle"]
            (label "message-text" "Text: ")
            (text-field "message-text" "")
            (submit-button "Send"))
        ]
        [:h2 "People in room: "]
        [:div {:id "chatroom-handles"}]
      ]))

