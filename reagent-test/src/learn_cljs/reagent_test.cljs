(ns ^:figwheel-hooks learn-cljs.reagent-test
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [reagent.core :as r]                         ;; <1>
   [reagent.ratom :as ratom]
   
   )
  )

(println "This text is printed from src/learn_cljs/reagent_test.cljs. Go ahead and edit it and see reloading in action.")

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


(def a-cell (r/atom 0))                                    ;; <3>
(def b-cell (r/atom 0))

(def c-cell
  (ratom/make-reaction                                     ;; <4>
    #(+ @a-cell @b-cell))
)


(def a (gdom/getElement "cell-a"))
(def b (gdom/getElement "cell-b"))
(def c (gdom/getElement "cell-c"))


(defn update-cell [cell]
  (fn [e]
    (let [num (js/parseInt (.. e -target -value))]
      (reset! cell num))))


(gevents/listen a "change" (update-cell a-cell))           ;; <5>
(gevents/listen b "change" (update-cell b-cell))


(ratom/run!                                                ;; <6>
  (set! (.-value c) @c-cell))