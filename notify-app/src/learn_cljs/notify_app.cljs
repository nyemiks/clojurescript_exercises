(ns ^:figwheel-hooks learn-cljs.notify-app
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]
   [learn-cljs.messaging :as msg]
   [learn-cljs.form-ui :as ui]
   [learn-cljs.notification :as notify]
   )
   )

(println "This text is printed from src/learn_cljs/notify_app.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))



(defn get-app-element []
  (gdom/getElement "app"))


(defn app-ui []
 [:div.container
    [notify/notifications]
    [ui/input-form]]
  
)


(defn mount [el]
;  (rdom/render [hello-world] el)
  (rdom/render [app-ui] el)  ;;;
  )

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
