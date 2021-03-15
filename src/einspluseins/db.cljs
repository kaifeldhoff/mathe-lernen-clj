(ns einspluseins.db)

(def default-db
  {:active-page :user-selection
   :answer ""
   :task-data nil
   ; TODO: set by user from task-data
   :max-answer-digits 2
   :audio {}
   }
  )
