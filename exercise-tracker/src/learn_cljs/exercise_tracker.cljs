(ns ^:figwheel-hooks learn-cljs.exercise-tracker
  (:require
   [goog.dom :as gdom]
   [reagent.dom :as rdom]
   [reagent.core :as r]
    [reagent.ratom :as ratom]
   )
)

(println "This text is printed from src/learn_cljs/exercise_tracker.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))






;; define your app data so that it doesn't get over-written on reload
;(defonce app-state (atom {:text "Hello world!"}))


(defn- current-date-string [d]
  (let [pad-zero #(.padStart (.toString %) 2 "0")
        y (.getFullYear d)
        m (-> (.getMonth d) inc pad-zero)
        d (pad-zero (.getDate d))]
    (str y "-" m "-" d)))

;


(defn initial-inputs []
  {:date (current-date-string (js/Date.))
   :minutes "0"})


(defonce state
  (r/atom {
             :inputs  (initial-inputs) 
             :entries {}
          }
  )
)


(defn get-app-element []
  (gdom/getElement "app"))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)


(defn hello []
  [:p "Hello World"]
  )

;; $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    (defn- random-point []
  (js/Math.floor (* (js/Math.random) 100)))

(defonce chart-data
  (let [points (map random-point (range 30))]              ;; <1>
    (r/atom {:points points
             :chart-max (reduce max 1 points)})))

(def chart-width 400)
(def chart-height 200)
(def bar-spacing 2)


(defn get-points [entries]
  (let [ms-in-day 86400000
        chart-days 30
        now (js/Date.now)]
    (map (fn [i]
           (let [days-ago (- chart-days (inc i))
                 date (current-date-string (js/Date. (- now (* ms-in-day days-ago))))]
             (get entries date 0)))
         (range chart-days))))



(defn chart2 []
  (let [
        {:keys [points chart-max]} @chart-data             ;; <2>
        bar-width (- (/ chart-width (count points)) bar-spacing)
       ]
    [:svg.chart {:x 0 :y 0
                 :width chart-width :height chart-height}
      (for [[i point] (map-indexed vector points)          ;; <3>
            :let [x (* i (+ bar-width bar-spacing))        ;; <4>
                  pct (- 1 (/ point chart-max))
                  bar-height (- chart-height (* chart-height pct))
                  y (- chart-height bar-height)]]
        [:rect {:key i                                     ;; <5>
                :x x :y y
                :width bar-width
                :height bar-height}])]))



  (defn chart []
  (let [
        entries (r/cursor state [:entries])
        chart-data (ratom/make-reaction
                    #(let [points (get-points @entries)]
                       {:points points
                        :chart-max (reduce max 1 points)
                        }
                      )
                   )
       ]

    (fn []
      (let [{:keys [points chart-max]} @chart-data
            bar-width (- (/ chart-width (count points))
                         bar-spacing)]
        [:svg.chart {:x 0 :y 0
                     :width chart-width :height chart-height}
         (for [[i point] (map-indexed vector points)
               :let [x (* i (+ bar-width bar-spacing))
                     pct (- 1 (/ point chart-max))
                     bar-height (- chart-height (* chart-height pct))
                     y (- chart-height bar-height)]]
           [:rect {:key i
                   :x x :y y
                   :width bar-width
                   :height bar-height}])]))))


;;  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  
  (defn date-input2 []
  [:div.input-wrapper                                      ;; <1>
    [:label "Day"]
    [:input {
             :type "date" 
             :value (get-in @state [:inputs :date])
             :on-change #(swap! state assoc-in [:inputs :date] (.. % -target -value))
             }
    ]
    ]
  )                              ;; <2>



  (defn date-input 
  "Uses a cursor that references the location
  of the state we are in interested in."
  []
  (let [val (r/cursor state [:inputs :date])]              ;; <1>
    (fn []
      [:div.input-wrapper
        [:label "Day"]
        [:input {:type "date"
                 :value @val                               ;; <2>
                 :on-change #(reset! val                   ;; <3>
                               (.. % -target -value))}]])))


    (defn time-input2 []
  [:div.input-wrapper
    [:label "Time (minutes)"]
    [:input {
              :type "number" :min 0 :step 1
              :value (get-in @state [:inputs :minutes])
            }
    ]
    ]
    )


    (defn time-input
    "Uses a cursor that references the location
  of the state we are in interested in."
    []
    (let
        [
          val  (r/cursor state [:inputs :minutes])
        ]
        (fn []
            [:div.input-wrapper
               [:label "Time (minutes)"]
               [:input 
                    {
                       :type "number" :min 0 :step 1
                       :value @val
                       :on-change #(reset! val (.. % -target -value))
                    }
               ]
            ]
        )
    )
    )



    (defn submit-button []
  [:div.actions
    [:button {:type "submit"} "Submit"]])


    (defn submit-form [state]
  (let [{:keys [date minutes]} (:inputs state)]
    (-> state
        (assoc-in [:entries date] (js/parseInt minutes))
        (assoc :inputs (initial-inputs)))))


    (defn form []
  [:form.input-form {:on-submit (fn [e]
                                  (.preventDefault e)
                                  (swap! state submit-form))}
    [date-input] 
    ;[date-input2]  
     [time-input]                                         ;; <3>
   ; [time-input2]
     
    [submit-button]
    ]
    )


  (defn app []
  [:div.app 
     [chart]
   ; [chart2]
     [form]
  ]
  )

  (rdom/render
  [app]                                                   ;; <1>
  (gdom/getElement "app"))                                 ;; <2>



