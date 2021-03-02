(ns einspluseins.events
  (:require
   [re-frame.core :as rf]
   #_[re-frame.db :as rfdb]
   #_[day8.re-frame.http-fx]
   #_[cljs-bach.synthesis :as bach]
   #_[ajax.core :as ajax]
   #_[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [einspluseins.db :refer [default-db]]
   [einspluseins.tasks :refer [levels commit-solution]]
   [einspluseins.audio :as audio]
   ))

(def clear-answer
  (rf/->interceptor
   :id      :clear-answer
   :after  (fn [context]
             (assoc-in context [:effects :db :answer] ""))))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   (let [level 1
         {:keys [:to-solve :create-fn]} (levels level)
         tasks (shuffle (create-fn))]
     (-> default-db
         (assoc-in [:remaining-tasks level] tasks)
         (assoc :to-solve to-solve)))))

(rf/reg-event-db
 ::add-digit
 (fn [db [_ digit]]
   (let [old-answer (db :answer)
         max-answer-digits (db :max-answer-digits)]
     (if (> max-answer-digits (count old-answer))
       (let [new-answer (str old-answer digit)]
         (assoc db :answer new-answer))
       db))))

(rf/reg-event-db
 ::clear
 (fn [db [_ _]]
   (assoc db :answer "")))

(rf/reg-event-db
 ::commit-solution
 [clear-answer]
 (fn [db _]
   (commit-solution db)))

(rf/reg-event-db
 ::load-audio
 (fn [db _]
   (let [db (-> db
                (assoc :audio (audio/load-audio))
                (assoc :show-start-modal false))]
     (audio/play-try-again (:audio db))
     (audio/play-applause (:audio db))
     db)))
