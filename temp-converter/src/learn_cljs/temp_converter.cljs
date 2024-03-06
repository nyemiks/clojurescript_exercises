(ns ^:figwheel-hooks learn-cljs.temp-converter
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   ))

(println "This text is printed from src/learn_cljs/temp_converter.cljs. Go ahead and edit it and see reloading in action.")


(def celsius-radio (gdom/getElement "unit-c"))
(def fahrenheit-radio (gdom/getElement "unit-f"))
(def kelvin-radio (gdom/getElement "unit-k"))

(def temp-input (gdom/getElement "temp"))
(def output-target (gdom/getElement "temp-out"))
(def output-unit-target (gdom/getElement "unit-out"))
(def clear-btn (gdom/getElement "clearBtn"))

(defn f->c [deg-f]
  (/ (- deg-f 32) 1.8))


(defn c->f [deg-c]
  (+ (* deg-c 1.8) 32))

(defn f->k [deg-f]
 (+ (* (- deg-f 32) (/ 5 9) ) 273.15 )
)

(defn c->k [deg-c]
 (+ (* deg-c (/ 9 5)) 32)
)

(defn k->f [deg-k]
(+ (* (- deg-k 273.15) (/ 9 5) ) 32)
)

(defn k->c [deg-k]
 (- deg-k 273.15)
)



(defn get-input-unit []
  (if (.-checked celsius-radio)
    :celsius
    :fahrenheit))


(defn get-input-unit2 []
(cond
  (.-checked celsius-radio) :celsius
  (.-checked fahrenheit-radio) :fahrenheit
  (.-checked kelvin-radio) :kelvin
  )
)


(defn get-input-unit3 []
(cond
  (.-checked celsius-radio) :celsius
  (.-checked fahrenheit-radio) :fahrenheit
  (.-checked kelvin-radio) :kelvin
  )
)

(defn get-input-temp []
  (js/parseInt (.-value temp-input)))

(defn set-output-temp [temp]
  (gdom/setTextContent output-target
                       (.toFixed temp 2)))

(defn clear-temp-input[]
(gdom/setProperties temp-input #js {"value" ""})
)


(defn update-output [_]
  (if (= :celsius (get-input-unit))
    (do (set-output-temp (c->f (get-input-temp)))
        (gdom/setTextContent output-unit-target "F"))
    (do (set-output-temp (f->c (get-input-temp)))
        (gdom/setTextContent output-unit-target "C"))))

(defonce is-initialized?
  (do     
   (println " --- is initialized ? ----")                                                 ;; <1>
    (gevents/listen temp-input "keyup" update-output)
    (gevents/listen celsius-radio "click" update-output)
    (gevents/listen fahrenheit-radio "click" update-output)
    (gevents/listen clear-btn "click" clear-temp-input)

    true)
    
)





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
