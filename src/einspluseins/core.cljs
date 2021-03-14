(ns einspluseins.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as rf]
   [re-pressed.core :as rp]
   [einspluseins.events :as events]
   [einspluseins.views :as views]
   [einspluseins.config :as config]
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-component] root-el)))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (rf/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (dev-setup)
  (mount-root))

(comment

  ; enter shadow-repl
  (shadow/repl :main)

  )