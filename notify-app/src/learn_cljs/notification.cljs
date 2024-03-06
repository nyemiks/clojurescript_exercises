(ns learn-cljs.notification
(:require 
          [cljs.core.async :refer [go-loop pub sub chan <! put!]]
          [reagent.core :as r]
          [learn-cljs.messaging :as msg]
)

)



(def initial-state
  {:messages []
   :next-id 0})


(defn add-notification [state id text]
  (.log js/console  "id: " id)
  (.log js/console  "text: " text)
  (-> state
      (update :messages conj {:id id
                              :text text})
      (assoc :next-id (inc id))))

(defn remove-notification [state id]
  (update state :messages
    (fn [messages]
      (filterv #(not= id (:id %)) messages))))             ;; <1>


(defn listen-for-added! [state]
  (.log js/console  "--  listen-for-added!  -- ")
 ; (.log js/console  state)
  (let [added (chan)]
    (sub msg/msg-bus add-notification added)               ;;
    (go-loop []
      (let [text (::payload (<! added))
            id (:next-id @state)]
        (swap! state add-notification id text)
        (js/setTimeout #(swap! state remove-notification id) 10000)
        (recur)))))


(defn notifications []
  (let [state (r/atom initial-state)]                      ;; <2>
    (listen-for-added! state)                              ;; <3>
    (fn []
      [:div.messages
        (for [msg (:messages @state)                       ;; <4>
              :let [{:keys [id text]} msg]]
          ^{:key id}
          [:div.notification.is-info
            [:button.delete {:on-click #(swap! state remove-notification id)}]
            [:div.body text]])])))