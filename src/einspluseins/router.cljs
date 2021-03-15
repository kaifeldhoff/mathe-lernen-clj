(ns einspluseins.router
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]))

(def routes
  ["/" {""     :user-selection
        "play" :play}])

(def history
  (let [dispatch #(rf/dispatch [:set-active-page {:page (:handler %)}])
        match #(bidi/match-route routes %)]
    (pushy/pushy dispatch match)))

(defn start! []
  (pushy/start! history))

(def url-for
  (partial bidi/path-for routes))
