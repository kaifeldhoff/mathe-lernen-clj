(ns einspluseins.core
  (:require
   [reagent.dom :as rd]
   [re-frame.core :as rf]
   [einspluseins.events :as events]
   [einspluseins.views :as views]
   [einspluseins.config :as config]
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (rd/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
