(ns ^:figwheel-hooks learn-cljs.doing-io
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]))

(println "This text is printed from src/learn_cljs/doing_io.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!" :input1 "" :input2 ""}))

(def input (gdom/getElement "pwd"))

(def input2 (gdom/getElement "pwd2"))

(def status (gdom/getElement "status"))

;(def status (.querySelector js/document "#status"))

(defn update-status[msg]
   (println "status: " msg)
   (gdom/setTextContent status msg)
)

(defn compare-input[]

 (if (= (:input1 @app-state) (:input2 @app-state))
  (update-status "they match")
  (update-status "they do not match")
 )
)

(defn get-input1[evt]
 ; (println "input1: " (.. evt -currentTarget -value))
  (println "input: " (.. evt -currentTarget -value))
   (swap! app-state assoc :input1 (.. evt -currentTarget -value))
   (compare-input)

)


(defn get-input2[evt]
 ; (println "input1: " (.. evt -currentTarget -value))
  (println "input: " (.. evt -currentTarget -value))
 (swap! app-state assoc :input2 (.. evt -currentTarget -value))
  (compare-input)
)






(gevents/listen input                        ;; <2>
                            "keyup"
                            get-input1)

(gevents/listen input2                       ;; <2>
                            "keyup"
                            get-input2)

(defn get-app-element []
  (gdom/getElement "app"))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
