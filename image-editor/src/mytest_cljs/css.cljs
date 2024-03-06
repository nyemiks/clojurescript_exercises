(ns mytest-cljs.css
    (:require              
			  [clojure.string :as str]
			  )
	)
	
(def context 
{
 :column-start  0
 :column-end    1
 :row-start     0
 :row-end       1
 :row           2
 :column        3
 :curr-row      1
 :curr-col      1 
 :grid []
}
)


(defn generate-grid
[column-start column-end row-start row-end]
 {:column-start  column-start
   :column-end  column-end
   :row-start   row-start
   :row-end     row-end
 }
 )
 
 
 (defn update-grid
  [acc row col]
   (->
      acc
	  (assoc :column-start col)
	  (assoc :column-end (inc col))
	  (assoc :row-start row)
	  (assoc :row-end (inc row))
   )
 )
 
 
 


(defn add-column
[acc]
 ;  (println "-- add column -- ")
   (let [
          acc (update-grid acc (:curr-row acc) (:curr-col acc) )    ; update grid 
          acc (update acc :curr-col inc)   ;; update current col
          current-grid (generate-grid (:column-start acc) (:column-end acc) (:row-start acc) (:row-end acc))
         
          acc (assoc acc :grid (conj (:grid acc) current-grid))		; update list of grids	  
				
         ]
	   ; (println "acc ...")
		; (println acc)
		;  (println "grid ...")
		; (println updated-grid)		
      acc
   )
)


(defn add-row
[acc]
;(println "-- add row -- ")
(let [
       acc (assoc acc :curr-col 1)
	   acc (update acc :curr-row inc)
     ]
	 (add-column acc)
)
)



 (defn get-grid
  [acc item]
   ; (println " -- get-grid -- ")
	;(println " acc: " acc)
	;(println " item: " item)
   (cond
      (<= (:curr-col acc) (:column acc) )  (add-column acc)
      (> (:curr-col acc) (:column acc) )   (add-row acc) 
   )
 )


(defn get-css-grid
[item]
(let 
[
 grid-column-start  (keyword (str/join "" ["grid-" (name :column-start)  ]) )
 grid-column-end    (keyword (str/join "" ["grid-" (name :column-end)  ]) )
 grid-row-start     (keyword (str/join "" ["grid-" (name :row-start)  ]) )
 grid-row-end       (keyword (str/join "" ["grid-" (name :row-end)  ]) )
]
 {
   grid-column-start (:column-start item)
   grid-column-end   (:column-end item)
   grid-row-start    (:row-start item)
   grid-row-end      (:row-end item)
 }
)
)