(ns learn-cljs.messaging
(:require [cljs.core.async :refer [go-loop pub sub chan <! put!]])
)


(defonce msg-ch (chan 1))
(defonce msg-bus (pub msg-ch ::type))

(defn dispatch!
 ([type] (dispatch! type nil))
 ([type payload]
  (put! msg-ch {::type type
                ::payload payload})))