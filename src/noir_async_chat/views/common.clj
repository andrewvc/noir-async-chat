(ns noir-async-chat.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5 include-js]]
        [hiccup.element :only [javascript-tag link-to]]))

(defpartial layout [& content]
  (html5
    [:html {:class "no-js" :lang "en"}
    [:head [:meta {:charset "utf-8"}]
     [:title "Noir Async Chat"]
     [:meta {:name "description", :content "A Chat Room"}]
     [:meta {:name "author", :content "Andrew Cholakian"}]
     [:link {:rel "icon" :type "image/png" :href "/favicon.png"}]

     (include-css
       "/css/style.css"
       "/css/main.css")
     (include-js
       "/js/libs/modernizr-2.0.min.js"
       "/js/libs/respond.min.js"
       "/js/libs/script.js")
     (javascript-tag "try{Typekit.load();}catch(e){};")]
    [:body
     [:div {:id "container"}
       [:header
         [:h1
           (link-to "/" "Asynchronously Chatting with Noir")]]
       [:div {:id "main" :role "main"}
         content]
     ]
     (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js")
     (javascript-tag "window.jQuery || document.write('<script src=\"/js/libs/jquery-1.6.2.min.js\"><\\/script>');")
     (javascript-tag "
       $script(['/js/libs/underscore.min.js', '/js/json2.js'], function () {
         console.log($);
         $script(['/js/main.js']);
       });")
     ]]))
