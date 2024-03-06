(ns ^:figwheel-hooks mytest-cljs.image-editor
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [mytest-cljs.css :as css]
    [clojure.string :as str]
    [cljs.pprint :refer [pprint]]
   )
   )

(println "This text is printed from src/mytest_cljs/image_editor.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
;(defonce app-state (atom {:text "Hello world!"}))


(def image-url "https://picsum.photos/v2/list?limit=6")


;(def clearBtn (gdom/createElement "button") )




(defonce app-state (atom {
                           :text "Hello world!"
						   :images []
                         }
						 )
						 )





;;;


(defn calc-css-grid 
[items]
  (:grid (reduce css/get-grid css/context items) )
)


(defn transform-css [item]

  (str/join "" [(name (first item) ) ":" " " (second item) ";"] )  
)

(defn update-item-css[item-css]

(let 
     [
      updated-css (->
                  item-css
              (assoc :width "20vh")
              (assoc :height "10vw")
              )
      css-tuples   (into [] updated-css)   ; vector of tuples
    
      style   (into [] (map transform-css css-tuples))
     
     ]

;(println css-tuples)
;(println style); style
  ; css-tuples
    (str/join "" style)    ; multiple styles
)

)


(defn figure
"Returns a figure element <figure></figure>. 
which will be added to a div later"
[item item-css]
(let 
     [
      figure (gdom/createElement "figure") 

      img (gdom/createElement "img") 

      figcaption (gdom/createElement "figcaption") 

      style (update-item-css item-css)

     ]

        (gdom/setProperties figure #js {
                                        "style" style
                                       }
        )

        (gdom/setProperties img #js {
                                      "id" (inc (js/parseInt (:id item) ) )
                                      "src"  (:download_url item)
                                      "class" "gallery__img"
                                      ":alt" (str/join "Image " [(:id item)])
                                    }
        )

         
         (gdom/setTextContent figcaption (:author item) )


       (gdom/appendChild figure img) 
       (gdom/appendChild figure figcaption) 

      

      (.log js/console figure)

     figure
)
)


(defn figures
"appends children to container node"
 [container  images]
   (println " -- figures -- ")
  (let [
         img-size (count images)
		 img-list  (range 1 (inc img-size))
		 css-grid (map css/get-css-grid (calc-css-grid img-list )  )
		; figures (map figure img-list css-grid )
		figures-list  (into []  (map figure images css-grid ) )
       ]
    	
      (println "figure nodes: " (count figures-list))
    


    (doseq [afigure figures-list]
     
      (gdom/appendChild container afigure) 
    )

   

   ;  (.log js/console container)
    
  )
)




    

(defn get-app-element []
  ; (println "----- get app element -----")
  (gdom/getElement "app")
)



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)


(def divGallery (gdom/createElement "div"))


(defn clear-authors []
(println " -- clear authors -- ")
(let 
[
 ; captions (gdom/getElement "figcaption") 

  captions (gdom/getElementsByTagName "figcaption") 
]

;(println "captions count: " (count captions))

(doseq
     [
      caption captions
    ]

     (gdom/setTextContent caption nil)
)

)

)

(defn clear-img-nodes[]
 (println "-- clear-img-nodes -- ")
;  (gdom/removeChildren divGallery)
 ; (println "has children" (has-child-nodes divGallery))   
 (let 
  [
   ; divGallery  (gdom/getElement "my-gallery")
  ]
  (do (gdom/removeChildren divGallery))
 )
)

(defn gallery []
  (println "-- gallery -- ")
  (let 
  [
   ; divGallery  (gdom/getElement "my-gallery")
  ]

 (println "my gallery: " divGallery)
    (.log js/console divGallery)

  (if (> (count (:images @app-state)) 0)   
     (figures divGallery  (:images @app-state) )
     divGallery
  )

  )
  
 
 ;(into [:div.gallery]
 ;  ()
 ;)
 
)


(defn render[]
(gallery)
)

(defn hide-author
[image-array]
  (swap! app-state assoc :images (into [] (map #(assoc % :author nil) image-array) ) ) 
   (clear-authors)
)

(defn clear-images
[image-array]
  (swap! app-state assoc :images (into [] (map #(assoc % :download_url nil) image-array) ) ) 
  (clear-img-nodes)
)

(defn update-images
[image-array]
  (println "-- update-images --")
  (swap! app-state assoc :images image-array )
 ; (clear-img-nodes)
  (render)
)

(defn fetch-images
[]
(-> 
 (js/fetch image-url)
(.then (fn [response] (.json response)))
;(.then (fn [json] (println (js->clj json :keywordize-keys true))))
(.then (fn [json] 
          (update-images (js->clj json :keywordize-keys true) )  
        )
)
)
)


;; #############################


(defonce is-initialized?
  (do                                                      ;; <1>
  ;  (.setItem js/localStorage "init-at" (.now js/Date))
   ; (js/alert "Welcome!")
     (println " -- is initialized -- ")
     (
      let [
        app     (get-app-element)

        divContainer (gdom/createElement "div")

      ;  divGallery (gdom/createElement "div")

        divBtn   (gdom/createElement "div")

       clearBtn (gdom/createElement "button") 
    

       hideBtn    (gdom/createElement "button") 
    

       fetchBtn    (gdom/createElement "button") 
    
       

      ]
       
     
        (gdom/setProperties clearBtn #js {"id" "clearBtn"})
        (gdom/setTextContent clearBtn "Clear Images")

         (gdom/setProperties hideBtn #js {"id" "hideBtn"})
          (gdom/setTextContent hideBtn "Hide Author")

          (gdom/setProperties fetchBtn #js {"id" "fetchBtn"})
          (gdom/setTextContent fetchBtn "Fetch Image")

;;  divBtn
        ; (gdom/setTextContent divBtn "Clear")

         (gevents/listen clearBtn "click"
                  #(clear-images (:images @app-state)))

  (gevents/listen hideBtn "click"
                  #(hide-author (:images @app-state)))


         (gevents/listen fetchBtn "click"
                  #(fetch-images))

        (gdom/appendChild divBtn clearBtn)      
        (gdom/appendChild divBtn hideBtn)
        (gdom/appendChild divBtn fetchBtn)


         (gdom/setProperties divGallery #js {
          "class" "gallery"
          "id"  "my-gallery"
          }
         )

         (gdom/setProperties divContainer #js {"class" "container"})

         (gdom/appendChild divContainer divGallery) 

          (gdom/appendChild app divBtn) 
         (gdom/appendChild app divContainer)  

     )

    true))                                                 ;; <2>


