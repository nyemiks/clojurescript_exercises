(ns ^:figwheel-hooks cljs-weather.weather
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]
   [ajax.core :as ajax]
   ))

(println "This text is printed from src/cljs_weather/weather.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
; (defonce app-state (atom {:text "Hello world!"}))

 (defonce app-state (atom {:title "WhichWeather"
                            :postal-code ""
                            :temperatures {:today {:label "Today"
                                                   :value nil}
                                           :tomorrow {:label "Tomorrow"
                                                      :value nil}}}))

(defn get-app-element []
  (gdom/getElement "app"))




  (defn hello-world []                                       ;; <2>
  [:div
   [:h1 {:class "app-title"} "Hello, World"]])


(defn title []
  [:h1 (:title @app-state)])



(defn temperature [temp]                                   ;; <1>
  [:div {:class "temperature"}
   [:div {:class "value"}
    (:value temp)]
   [:h2 (:label temp)]])


(defn go []
 [:button {:on-click get-forecast!} "Go"]
)

(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Enter your postal code"]
   [:input {:type "text"
            :placeholder "Postal Code"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state assoc :postal-code (-> % .-target .-value))
            }]
   [go]
   ]
   )


(defn handle-response [resp]
  (let [today (get-in resp ["list" 0 "main" "temp"])       ;; <1>
        tomorrow (get-in resp ["list" 8 "main" "temp"])]
    (swap! app-state                                       ;; <2>
        update-in [:temperatures :today :value] (constantly today))
    (swap! app-state
        update-in [:temperatures :tomorrow :value] (constantly tomorrow))))


        (defn get-forecast! []
  (let [postal-code (:postal-code @app-state)]             ;; <1>
    (ajax/GET "http://api.openweathermap.org/data/2.5/forecast"
         {:params {"q" postal-code
                   "appid" "c59453eeeb460978d944e94ba9af433b"   ;; api key
                   "units" "imperial"}
          :handler handle-response})))     



(defn app []
  [:div {:class "app"}
   [title]                                                 ;; <2>
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]          ;; <3>
      [temperature temp])]
   [postal-code]])


(defn mount [el]
  (rdom/render [app] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
