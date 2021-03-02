(ns einspluseins.subs
  (:require [re-frame.core :as rf]
            [einspluseins.tasks :refer [levels task->str]]
            #_[cljs.pprint :as pprint]))

(rf/reg-sub
 ::progress
 :<- [::remaining-to-solve]
 :<- [::total-tasks]
 (fn [[remaining-to-solve total-tasks] _]
   (let [value (* 100 (/ remaining-to-solve total-tasks))]
     (quot (* (+ value 0.5) 10) 10))))

(rf/reg-sub
 ::current-task
 :<- [::remaining-tasks-all-levels]
 :<- [::current-level]
 (fn [[remaining-tasks-all-levels current-level] _]
   (let [remaining-task-lists-of-level (remaining-tasks-all-levels current-level)
         actual-task-list (peek remaining-task-lists-of-level)]
     (when-let [task (peek actual-task-list)]
       (task->str task)))))

(rf/reg-sub
 ::total-tasks
 :<- [::current-level]
 (fn [current-level _]
   (get-in levels [current-level :to-solve])))

(rf/reg-sub
 ::remaining-to-solve
 (fn [db]
   (db :to-solve)))

(rf/reg-sub
 ::remaining-tasks-all-levels
 (fn [db]
   (db :remaining-tasks)))

(rf/reg-sub
 ::current-answer
 (fn [db]
   (db :answer)))

(rf/reg-sub
 ::current-level
 (fn [db]
   (db :current-level)))

(rf/reg-sub
 ::show-start-modal
 (fn [db]
   (db :show-start-modal)))

(rf/reg-sub
 ::db
 (fn [db]
   db))

(comment

  (-> @(rf/subscribe [::current-task]))
  ;; => ("4" "+" "2")

  ;; => ("1" "+" "6")






   
  
  )