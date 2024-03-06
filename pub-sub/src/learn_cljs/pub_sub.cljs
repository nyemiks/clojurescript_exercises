(ns ^:figwheel-hooks learn-cljs.pub-sub
  (:require
   [goog.dom :as gdom]
   [cljs.core.async :refer [chan <! >! timeout pub sub unsub unsub-all]]
   )
   (:require-macros [cljs.core.async.macros :refer [go]])
)

(println "This text is printed from src/learn_cljs/pub_sub.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)


;  publisher subscriber 

; publisher is just a normal channel
(def publisher (chan))


; publication is a thing we subscribe to
(def publication
  (pub publisher #(:topic %)))


; define a bunch of subscribers
(def subscriber-one (chan))
(def subscriber-two (chan))
(def subscriber-three (chan))


; subscribe
(sub publication :account-created subscriber-one)
(sub publication :account-created subscriber-two)
(sub publication :user-logged-in  subscriber-two)
(sub publication :change-page     subscriber-three)


(defn take-and-print [channel prefix]
  (go 
    (loop []
       (println prefix ": " (<! channel))
       (recur)
    )
   )
)