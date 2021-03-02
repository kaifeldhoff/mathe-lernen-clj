(ns einspluseins.tasks
  (:require [re-frame.core :as rf]
            [einspluseins.audio :as audio]))

(defn op->str [op]
  (let [m {+ '+
           - '-
           * '*
           / '/}]
    (m op)))

(defn task->str [task]
  (let [op (op->str (task :op))
        n1 (task :n1)
        n2 (task :n2)]
    (map #(str %) [n1 op n2])))

(defn range-10-add-1
  "Add 1 to any number from 1-9"
  []
  (let [max-int 10]
    (for [n (range 1 max-int)]
      (let [[n1 n2] (shuffle [n 1])]
        [{:op + :n1 n1 :n2 n2}]))))

(defn range-10-sub-1
  "Subtract 1 from any number from 2-10"
  []
  (let [max-int 10]
    (for [n1 (range 2 (inc max-int))]
      (let [n2 (rand-nth [(dec n1) 1])]
        [{:op - :n1 n1 :n2 n2}]))))

(defn range-10-add-any
  "Add any two number with result <=10"
  []
  (let [max-int 10]
    (for [n1 (range 1 max-int)
          n2 (range n1 (inc (- max-int n1)))]
      (let [[n1' n2'] (shuffle [n1 n2])]
        [{:op + :n1 n1' :n2 n2'}]))))

(defn range-11-20-add-0-9+10
  "Add any two number [0-9]+10 with result 11-20"
  []
  (let [max-int 10]
    (for [n1' (range 0 max-int)
          n2' (range (inc n1') (inc (- max-int n1')))]
      (let [[f1 f2] (shuffle [0 10])
            n1 (+ n1' f1)
            n2 (+ n2' f2)]
        [{:op + :n1 n1 :n2 n2}]))))

(defn range-11-20-add-1-9-then+1
  "Add any two number 1-9 result 11-20 followed by inc 1"
  []
  (vec
   (let [max-int 10]
     (for [n1 (range 2 max-int)
           n2' (range (- 11 n1) 8)]
       (vec
        (for [add [2 1 0]]
          (let [n2 (+ n2' add)]
            {:op + :n1 n1 :n2 n2 :res (+ n1 n2)})))))))

(defn range-10-19-add-1-9
  "Add any two number <10 with result 11-19"
  []
  (for [n1 (range 1 10)
        n2 (range (- 10 n1) 10)]
    [{:op + :n1 n1 :n2 n2}]))

(defn range-10-90-add-10th
  "Add any two 10th-numbers with result <=90"
  []
  (for [n1 (range 1 10)
        n2 (range n1 (- 10 n1))]
    (let [[n1' n2'] (shuffle [n1 n2])]
      [{:op + :n1 (* 10 n1') :n2 (* 10 n2')}])))

(defn range-9-add-any-then-10th
  "Add any two number with result <=9 followed by random 10th"
  []
  (vec
   (let [max-int 9]
     (for [n1 (range 1 max-int)
           n2 (range n1 (inc (- max-int n1)))]
       (vec
        (for [add (conj '(0) (rand-nth [1 2 3 4 5]) (rand-nth [6 7 8 9]))]
          (let [add-10th (* 10 add)]
            {:op + :n1 (+ n1 add-10th) :n2 n2})))))))

#_(def raw-levels [{:to-solve 5 :create-fn range-10-add-1}
                 {:to-solve 5 :create-fn range-10-sub-1}
                 {:to-solve 5 :create-fn range-10-add-any}
                 {:to-solve 5 :create-fn range-11-20-add-0-9+10}
                 {:to-solve 6 :create-fn range-11-20-add-1-9-then+1}
                 {:to-solve 5 :create-fn range-10-19-add-1-9}
                 {:to-solve 5 :create-fn range-10-90-add-10th}
                 {:to-solve 6 :create-fn range-9-add-any-then-10th}
                 {:to-solve 1 :create-fn (fn [_] nil)}])

;; Kjell

(defn multi-1-and-5-range-1-5
  "multiply 1 and 5 with range 1 to 5"
  []
  (vec
   (for [n1 [1 5]
         n2 (range 1 6)]
     (let [[n1' n2'] (shuffle [n1 n2])]
       [{:op * :n1 n1' :n2 n2'}]))))

(defn multi-1-and-5-range-6-10
  "multiply 1 and 5 with range 6 to 10"
  []
  (vec
   (for [n1 [1 5]
         n2 (range 6 11)]
     (let [[n1' n2'] (shuffle [n1 n2])]
       [{:op * :n1 n1' :n2 n2'}]))))

(defn multi-1-and-5-range-1-10
  "multiply 1 and 5 with range 1 to 10"
  []
  (vec
   (for [n1 [1 5]
         n2 (range 1 11)]
     (let [[n1' n2'] (shuffle [n1 n2])]
       [{:op * :n1 n1' :n2 n2'}]))))

(comment
  (range 1 6)
  
  )


(def raw-levels [{:to-solve 3 :create-fn multi-1-and-5-range-1-5}
                 {:to-solve 3 :create-fn multi-1-and-5-range-6-10}
                 {:to-solve 3 :create-fn multi-1-and-5-range-1-10}
                 ])

(def levels (into {} (map vector (iterate inc 1) raw-levels)))

(defn solve [task]
  (let [op (task :op)
        n1 (task :n1)
        n2 (task :n2)]
    (op n1 n2)))

(defn answer-correct? [task answer]
  (let [solution (solve task)
        integer-answer (js/parseInt answer)]
    (= integer-answer solution)))

(defn set-next-task [{:keys [remaining-tasks current-level to-solve] :as db}]
  (let [new-to-solve (dec to-solve)
        remaining-task-lists-of-level (remaining-tasks current-level)
        db (assoc db :to-solve new-to-solve)]
    (if (= 1 (count (peek remaining-task-lists-of-level)))
      (if (empty? (pop remaining-task-lists-of-level))
        (let [{:keys [:create-fn]} (levels current-level)
              tasks (shuffle (vec (create-fn)))]
          (assoc-in db [:remaining-tasks current-level] tasks))
        (update-in db [:remaining-tasks current-level] (comp shuffle pop)))
      (update-in db [:remaining-tasks current-level] #(conj (pop %) (pop (peek %)))))))

(defn set-next-level [{:keys [current-level] :as db}]
  (let [next-level (inc current-level)
        {:keys [:to-solve :create-fn]} (levels next-level)
        tasks (shuffle (vec (create-fn)))]
    (-> db
        (assoc :current-level next-level)
        (assoc-in [:remaining-tasks next-level] tasks)
        (assoc :to-solve to-solve))))

(defn process-correct-answer [{:keys [to-solve audio] :as db}]
  (audio/play-applause audio)
  (if (zero? (dec to-solve))
    (set-next-level db)
    (set-next-task db)))

(defn process-wrong-answer [{:keys [current-level audio] :as db}]
  (audio/play-try-again audio)
  (let [total-tasks (get-in levels [current-level :to-solve])]
  (-> db
      (update-in [:remaining-tasks current-level] shuffle)
      (assoc-in [:to-solve] total-tasks))))

(defn get-current-task [{:keys [remaining-tasks current-level]}]
  (let [remaining-task-lists-of-level (remaining-tasks current-level)
        actual-task-list (peek remaining-task-lists-of-level)
        current-task (peek actual-task-list)]
    current-task))

(defn commit-solution [{:keys [answer] :as db}]
  (let [current-task (get-current-task db)]
    (if-not (empty? answer)
      (if (answer-correct? current-task answer)
        (process-correct-answer db)
        (process-wrong-answer db))
      db)))

(comment

  (cljs.pprint/pprint (range-10-add-1))
  (cljs.pprint/pprint (range-10-sub-1))
  (cljs.pprint/pprint (range-10-add-any))
  (cljs.pprint/pprint (range-10-19-add-1-9))
  (cljs.pprint/pprint (range-10-90-add-10th))



  (answer-correct? {:op + :n1 1 :n2 2} 1)
  ;; => false

  #_@rfdb/app-db

  #_(get-current-task @rfdb/app-db)
  ;; => {:op #object[cljs$core$_PLUS_], :n1 1, :n2 5}


  (rf/dispatch [::add-digit "9"])
  ;; => nil

  #_(-> (get-in @rfdb/app-db [:remaining-tasks 1])
      peek
      type)
  ;; => cljs.core/PersistentVector

  (update-in {1 [[1 2] [3 4]]} [1 0] pop)
  ;; => {1 [[1] [3 4]]}
  )
