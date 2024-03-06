(ns learn-cljs.form-ui
(:require 
   [reagent.core :as r]
   [learn-cljs.messaging :as msg]
   [learn-cljs.notification :as notify]
)
)


(defonce form-state (r/atom {:first-name ""                ;; <1>
                             :last-name ""}))

(defn update-value [e field]                               ;; <2>
  (swap! form-state assoc field (.. e -target -value)))

(defn submit-form []                                       ;; <3>
  (let [{:keys [first-name last-name]} @form-state]
    (msg/dispatch! notify/add-notification
      (str "Welcome, " first-name " " last-name)))
  (swap! form-state assoc :first-name ""
                          :last-name ""))

(defn input-field [key label]                              ;; <4>
  [:div.field
    [:label.label label
    [:div.control
      [:input.input {:value (get @form-state key)
                     :on-change #(update-value % key)}]]]])

(defn input-form []
  [:div.form
    [input-field :first-name "First Name"]                 ;; <5>
    [input-field :last-name "Last Name"]
    [:div.field
      [:button.button {:on-click submit-form}
        "Add"]]])