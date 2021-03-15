(ns einspluseins.events
  (:require
   [re-frame.core :as rf]
   [re-frame.db :as rfdb]
   #_[day8.re-frame.http-fx]
   #_[cljs-bach.synthesis :as bach]
   #_[ajax.core :as ajax]
   #_[day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [einspluseins.db :refer [default-db]]
   [einspluseins.tasks :as tasks]
   [einspluseins.audio :as audio]
   ))

(rf/reg-event-fx
 ::set-active-page
 (fn [{:keys [db]} [_ {:keys [page]}]]
   {:db (assoc db :active-page page)}))

(def clear-answer
  (rf/->interceptor
   :id      :clear-answer
   :after  (fn [context]
             (assoc-in context [:effects :db :answer] ""))))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   default-db))

(rf/reg-event-db
 ::add-digit
 (fn [db [_ digit]]
   (let [old-answer (db :answer)
         ; TODO: Get by user
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
   (if (:complete db)
     db
     (assoc db :task-data (tasks/solve (:task-data db) (:answer db))))))

(rf/reg-event-db
 ::init-audio-and-set-user
 (fn [db [_ username]]
   (let [user (keyword username)
         level-data (tasks/init-with-user user)]
     (-> db
         (assoc :active-page :play)
         (assoc :audio (audio/load-audio))
         (assoc :show-start-modal false)
         (assoc :task-data level-data)))))

(comment

  (let [db @rfdb/app-db]
    db #_(get-in db [:answer]))

  (swap! rfdb/app-db assoc-in [:task-data :current-task] nil)

  (rf/dispatch-sync [::commit-solution])

  (rf/dispatch-sync [::add-digit "8"])


  )