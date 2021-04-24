(ns einspluseins.tasks
  (:require [re-frame.db :as rfdb]))

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Kjres
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn range-10-add-1
  "Add 1 to any number from 1-9"
  []
  (let [max-int 10]
    (for [n (range 1 max-int)]
      (let [[n1 n2] (shuffle [n 1])]
        [{:op + :n1 n1 :n2 n2}]))))

(defn range-10-add-sub-ns
  "Add and subtract n from any number range 10"
  [ns]
  (for [n ns
        n1 (range 1 (- 11 n))]
    (let [n2 n
          sum (+ n1 n2)]
      [{:op - :n1 sum :n2 n2}
       {:op + :n1 n1 :n2 n2}])))

(comment

  (map #(+ 2 %) (range 1 9))

  (range-10-add-sub-ns [7 8 ])
  )

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
  (for [n1 (range 2 10)
        n2 (range (- 10 n1) 10)]
    [{:op + :n1 n1 :n2 n2}]))

(defn range-10-90-add-10th
  "Add any two 10th-numbers with result <=90"
  []
  (for [n1 (range 2 10)
        n2 (range n1 (- 10 n1))]
    (let [[n1' n2'] (shuffle [n1 n2])]
      [{:op + :n1 (* 10 n1') :n2 (* 10 n2')}])))

(defn range-9-add-any-then-10th
  "Add any two number with result <=9 followed by random 10th"
  []
  (vec
   (let [max-int 9]
     (for [n1 (range 2 max-int)
           n2 (range n1 (inc (- max-int n1)))]
       (vec
        (for [add (conj '(0) (rand-nth [1 2 3 4 5]) (rand-nth [6 7 8 9]))]
          (let [add-10th (* 10 add)]
            {:op + :n1 (+ n1 add-10th) :n2 n2})))))))

(defn plus-minus-same-10th-add-10th
  ""
  []
  (vec
    (for [n1 (range 2 10)
          n2 (range n1 (- 10 n1))]
      (vec
        (let [sum (+ n1 n2)
              add-10th (* 10 (rand-nth (range 1 9)))]
          (shuffle
            [{:op + :n1 (+ n1 add-10th) :n2 n2}
             {:op - :n1 (+ sum add-10th) :n2 n1}
             {:op - :n1 (+ sum add-10th) :n2 n2}]))))))

(defn plus-minus-same-10th-add-rand-10th
  ""
  []
  (vec
    (for [n1 (range 2 10)
          n2 (range n1 (- 10 n1))]
      (vec
        (let [sum (+ n1 n2)
              get-10tha #(* 10 (rand-nth (range 1 9)))]
          (shuffle
            [{:op + :n1 (+ n1 (get-10tha)) :n2 n2}
             {:op - :n1 (+ sum (get-10tha)) :n2 n1}
             {:op - :n1 (+ sum (get-10tha)) :n2 n2}]))))))

(defn plus-minus-switch-10th-add-10th
  ""
  []
  (vec
    (for [n1 (range 2 10)
          n2 (range (- 10 n1) 11)]
      (vec
        (let [sum (+ n1 n2)
              add-10th (* 10 (rand-nth (range 1 9)))]
          (shuffle
            [{:op + :n1 (+ n1 add-10th) :n2 n2}
             {:op - :n1 (+ sum add-10th) :n2 n1}
             {:op - :n1 (+ sum add-10th) :n2 n2}]))))))

(defn plus-minus-switch-10th-add-rand-10th
  ""
  []
  (vec
    (for [n1 (range 2 10)
          n2 (range (- 10 n1) 11)]
      (vec
        (let [sum (+ n1 n2)
              get-10tha #(* 10 (rand-nth (range 1 9)))]
          (shuffle
            [{:op + :n1 (+ n1 (get-10tha)) :n2 n2}
             {:op - :n1 (+ sum (get-10tha)) :n2 n1}
             {:op - :n1 (+ sum (get-10tha)) :n2 n2}]))))))



(comment

  (for [n1 (range 2 10)
        n2 (range (- 10 n1) 11)]
    [n1 n2 (+ n1 n2)])


  (plus-minus-same-10th-add-10th)
  )


(def kres-raw-levels [{:to-solve 5 :create-fn range-10-add-1}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [1])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [2])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [3])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [4])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [5])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [5 6])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [6 7])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [7 8])}
                      {:to-solve 5 :create-fn #(range-10-add-sub-ns [7 8 9])}
                      {:to-solve 5 :create-fn range-10-add-any}
                      {:to-solve 5 :create-fn range-11-20-add-0-9+10}
                      {:to-solve 6 :create-fn range-11-20-add-1-9-then+1}
                      {:to-solve 5 :create-fn range-10-19-add-1-9}
                      {:to-solve 5 :create-fn range-10-90-add-10th}
                      {:to-solve 6 :create-fn range-9-add-any-then-10th}
                      {:to-solve 12 :create-fn plus-minus-same-10th-add-10th}
                      {:to-solve 12 :create-fn plus-minus-same-10th-add-rand-10th}
                      {:to-solve 12 :create-fn plus-minus-switch-10th-add-10th}
                      {:to-solve 12 :create-fn plus-minus-switch-10th-add-rand-10th}
                      ])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Kjell
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn multi-r1-up-to-r2-with-ms-no100
  "multiply range r1 to including r2 with all ms, no 100"
  [r1 r2 ms]
  (remove
    nil?
    (for [n1 ms
          n2 (range r1 (inc r2))]
      (let [[n1' n2'] (shuffle [n1 n2])]
        (when-not (= 10 n1' n2')
          [{:op * :n1 n1' :n2 n2'}])))))

(comment
  (multi-r1-up-to-r2-with-ms-no100 6 10 [1 5])
  )

(def kjell-raw-levels [{:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [1 5])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 10 [1 5])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 9 [5])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 9 [2 5])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 9 [2 3])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 9 [3 4])}
                       {:to-solve 10 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 9 [2 3 4 5])}

                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [4 6])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [4 8])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [3 9])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [7])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [6 9])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [7 8])}
                       {:to-solve 10 :create-fn #(multi-r1-up-to-r2-with-ms-no100 2 5 [6 7 8 9])}

                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [4 6])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [4 8])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [3 9])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [7])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [6 9])}
                       {:to-solve 5 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [7 8])}
                       {:to-solve 10 :create-fn #(multi-r1-up-to-r2-with-ms-no100 6 9 [6 7 8 9])}
                       ])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; general
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def levels-per-user {:Kjell {:levels (into {} (map vector (iterate inc 1) kjell-raw-levels))
                              :max-answer-digits 2}
                      :Kres  {:levels (into {} (map vector (iterate inc 1) kres-raw-levels))
                              :max-answer-digits 2}
                      })

(def data-structure
  {:current-user nil
   :current-task nil
   :current-task-block nil
   :pending-task-blocks nil
   :current-to-solve nil
   :current-level nil
   :level-data nil
   :last-answer-correct? nil
   :complete nil})


(defn solve-task [task]
  (let [op (task :op)
        n1 (task :n1)
        n2 (task :n2)]
    (op n1 n2)))

(defn answer-correct? [task answer]
  (let [solution (solve-task task)
        integer-answer (js/parseInt answer)]
    (= integer-answer solution)))

(defn create-pending-task-blocks [{:keys [level-data current-level]}]
  (let [create-fn (-> level-data
                      :levels
                      (get current-level)
                      :create-fn)]
    (shuffle (create-fn))))

(declare get-to-solve-of-level)

(defn get-or-create-current-task [data]
  (if (:current-task data)
    data
    (if (seq (:current-task-block data))
      (let [current-task-block (:current-task-block data)
            new-current-task (peek current-task-block)
            new-current-task-block (shuffle (pop current-task-block))
            ]
        (-> data
            (assoc :current-task-block new-current-task-block)
            (assoc :current-task new-current-task)))
      (let [pending-task-blocks (if (seq (:pending-task-blocks data))
                                  (:pending-task-blocks data)
                                  (create-pending-task-blocks data))
            temp-current-task-block (shuffle (peek pending-task-blocks))
            new-current-task-block (pop temp-current-task-block)
            new-current-task (peek temp-current-task-block)
            new-pending-task-blocks (shuffle (pop pending-task-blocks))]
        (-> data
            (assoc :current-task new-current-task)
            (assoc :current-task-block new-current-task-block)
            (assoc :pending-task-blocks new-pending-task-blocks))))))

(defn set-next-task [data]
  (-> data
      (update :current-to-solve dec)
      (dissoc :current-task)
      get-or-create-current-task))

(defn set-next-level [{:keys [current-level level-data] :as data}]
  (let [next-level (inc current-level)]
    (if (contains? (:levels level-data) next-level)
      (-> data
          (assoc :current-level next-level)
          (dissoc :current-task)
          (dissoc :current-task-block)
          (dissoc :pending-task-blocks)
          (get-or-create-current-task)
          (#(assoc % :current-to-solve (get-to-solve-of-level %))))
      (assoc data :complete true))))

(defn process-correct-answer [data]
  (let [data (assoc data :last-answer-correct? true)]
    (if (zero? (dec (:current-to-solve data)))
      (set-next-level data)
      (set-next-task data))))

(defn reset-to-solve [data]
  (let [new-to-solve (get-to-solve-of-level data)]
    (assoc data :current-to-solve new-to-solve)))

(defn process-wrong-answer [{:keys [current-level level-data] :as data}]
  (-> data
      reset-to-solve
      (assoc :last-answer-correct? false)
      (dissoc :current-task)
      (dissoc :current-task-block)
      (update :pending-task-blocks shuffle)
      get-or-create-current-task))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; interface
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn init-with-user [user]
  (let [level-data (get levels-per-user user)]
    (-> data-structure
        (assoc :complete false)
        (assoc :current-level 1)
        (assoc :current-user user)
        (assoc :level-data level-data)
        (#(assoc % :current-to-solve (get-to-solve-of-level %)))
        get-or-create-current-task)))

#_(defn get-current-task [data]
  (get-or-create-current-task data))

(defn get-to-solve-of-level [{:keys [current-level level-data]}]
  (-> level-data
      :levels
      (get current-level)
      :to-solve))

(defn solve [data answer]
  (if-let [current-task (:current-task data)]
    (if (answer-correct? current-task answer)
      (process-correct-answer data)
      (process-wrong-answer data))
    data))

#_(defn complete [data]
  (:complete data))

(comment

  (let [db @rfdb/app-db
        task-data (:task-data db)
        answer (:answer db)]
    answer)



  (init-with-user :Kres)

  (def data (-> (init-with-user :Kres)
                (assoc :current-level 8)
                (assoc :current-to-solve 1)
                ))

  data
  (solve data "2")

  (let [{:keys [current-level level-data]} data
        next-level (inc current-level)]
    (contains? (:levels level-data) next-level))



      (contains? (-> data :level-data :levels ) 2)

  data

  (-> (init-with-user :Kres)
      (assoc :current-level 8)
      get-or-create-current-task
      :current-task
      (answer-correct? nil)
      )

  (peek (seq [1]))

  (pop '(1))
  )
