(ns einspluseins.subs
  (:require [re-frame.core :as rf]
            [re-frame.db :as rfdb]
            [einspluseins.tasks :as tasks]
            #_[cljs.pprint :as pprint]))

(rf/reg-sub
 ::progress
 :<- [::remaining-to-solve]
 :<- [::total-tasks]
 (fn [[remaining-to-solve total-tasks] _]
   (let [value (* 100 (/ remaining-to-solve total-tasks))]
     (quot (* (+ value 0.5) 10) 10))))

(rf/reg-sub
 ::total-tasks
 :<- [::current-level]
 :<- [::level-data]
 (fn [[current-level level-data] _]
   (tasks/get-to-solve-of-level {:current-level current-level
                                 :level-data level-data})))

(rf/reg-sub
  ::current-task-display
  :<- [::current-task]
  :<- [::complete]
  (fn [[current-task complete] _]
    (if complete
      "FERTIG"
      (or current-task "..."))))

(rf/reg-sub
 ::remaining-to-solve
 (fn [db]
   (-> db :task-data :current-to-solve)))

(rf/reg-sub
 ::current-answer
 (fn [db]
   (db :answer)))

(rf/reg-sub
  ::current-task
  (fn [db]
    (when-let [current-task (-> db :task-data :current-task)]
      (tasks/task->str current-task))))

(rf/reg-sub
 ::current-level
 (fn [db]
   (-> db :task-data :current-level)))

(rf/reg-sub
 ::level-data
 (fn [db]
   (-> db :task-data :level-data)))

(rf/reg-sub
  ::complete
  (fn [db]
    (-> db :task-data :complete)))

(rf/reg-sub
 ::show-start-modal
 (fn [db]
   (db :show-start-modal)))

(rf/reg-sub
 ::db
 (fn [db]
   db))

(comment

  (let [db @rfdb/app-db]
    (get-in db [:task-data :current-task]))

  (swap! rfdb/app-db assoc-in [:task-data :current-task] nil)

  (swap! rfdb/app-db assoc-in [:task-data :complete] true)

  @(rf/subscribe [::progress])

  @(rf/subscribe [::total-tasks])

  @(rf/subscribe [::current-task-display])

  @(rf/subscribe [::remaining-to-solve])

  @(rf/subscribe [::current-task])

  @(rf/subscribe [::current-level])

  @(rf/subscribe [::level-data])

  @(rf/subscribe [::complete])

  @(rf/subscribe [::show-start-modal])

  (-> @(rf/subscribe [::show-start-modal])
      :task-data
      #_:complete)
  
  )
