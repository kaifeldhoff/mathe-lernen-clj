(ns einspluseins.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [re-pressed.core :as rp]
   #_[reagent.core :as r]
   [re-com.core :refer [h-box gap v-box box button progress-bar modal-panel label radio-button #_checkbox]]
   [cljs.pprint :refer [pprint]]
   [einspluseins.subs :as subs]
   [einspluseins.events :as events]
   ))

(defn dispatch-keydown-rules []
  (rf/dispatch
   [::rp/set-keydown-rules
    {:event-keys [
                  [[::events/add-digit "0"]
                   [{:keyCode 96}]]
                  [[::events/add-digit "1"]
                   [{:keyCode 97}]]
                  [[::events/add-digit "2"]
                   [{:keyCode 98}]]
                  [[::events/add-digit "3"]
                   [{:keyCode 99}]]
                  [[::events/add-digit "4"]
                   [{:keyCode 100}]]
                  [[::events/add-digit "5"]
                   [{:keyCode 101}]]
                  [[::events/add-digit "6"]
                   [{:keyCode 102}]]
                  [[::events/add-digit "7"]
                   [{:keyCode 103}]]
                  [[::events/add-digit "8"]
                   [{:keyCode 104}]]
                  [[::events/add-digit "9"]
                   [{:keyCode 105}]]
                  [[::events/commit-solution]
                   [{:keyCode 13}]]
                  [[::events/clear]
                   [{:keyCode 8}]]
                  ]
     :clear-keys [[{:keyCode 27} ;; escape
                   ]]}]))

(defn numpad []
  (let [btn-style {:font-size "10vh"
                   :width "10vw"}]
  [v-box
   :gap "2vh"
   :children [[h-box
               :gap "2vw"
               :children [[button :label "1" :style btn-style :on-click #(rf/dispatch [::events/add-digit "1"])]
                          [button :label "2" :style btn-style :on-click #(rf/dispatch [::events/add-digit "2"])]
                          [button :label "3" :style btn-style :on-click #(rf/dispatch [::events/add-digit "3"])]]]
              [h-box
               :gap "2vw"
               :children [[button :label "4" :style btn-style :on-click #(rf/dispatch [::events/add-digit "4"])]
                          [button :label "5" :style btn-style :on-click #(rf/dispatch [::events/add-digit "5"])]
                          [button :label "6" :style btn-style :on-click #(rf/dispatch [::events/add-digit "6"])]]]
              [h-box
               :gap "2vw"
               :children [[button :label "7" :style btn-style :on-click #(rf/dispatch [::events/add-digit "7"])]
                          [button :label "8" :style btn-style :on-click #(rf/dispatch [::events/add-digit "8"])]
                          [button :label "9" :style btn-style :on-click #(rf/dispatch [::events/add-digit "9"])]]]
              [h-box
               :gap "2vw"
               :children [[button :label "C" :style btn-style :on-click #(rf/dispatch [::events/clear])]
                          [button :label "0" :style btn-style :on-click #(rf/dispatch [::events/add-digit "0"])]
                          [button :label "↲" :style btn-style :on-click #(rf/dispatch [::events/commit-solution])]]]
              ]
   ]))

(defn task []
  (let [current-task (rf/subscribe [::subs/current-task])]
    (if-not (nil? @current-task)
      [h-box
       :size "1"
       :gap "1"
       :children (for [part @current-task]
                   [box :child part :style {:font-size "25vh"}])]
      [box :child "FERTIG!" :style {:font-size "20vh"}])))

(defn solution []
  (let [answer (rf/subscribe [::subs/current-answer])]
    [h-box
     :size "1"
     :children [[box :child @answer :style {:font-size "30vh"}]]]))

(defn to-solve []
  [v-box
   :size "1"
   :align :center
   :children [[task]
              [solution]]])

(defn show-progress-bar []
  (let [ratio (rf/subscribe [::subs/progress])
        current-level (rf/subscribe [::subs/current-level])]
    #_[:pre (with-out-str (pprint @ratio))]
    [h-box
     :size "1"
     :justify :center
     :align :center
     :children [[box :child (str "Level " @current-level) :style {:font-size "5vh"}]
                [gap :size "5vw"]
                [progress-bar
                 :model @ratio
                 :width "80vw"]]]))

(defn start-modal []
  (let [show (rf/subscribe [::subs/show-start-modal])
        user (reagent/atom "Kilian")]
    (when @show
      [modal-panel
       ; :backdrop-on-click #()
       :child [v-box
               :width    "300px"
               :align :center
               :children [[label :label "Wer bist Du?"]
                          [gap :size "20px"]
                          (doall
                           (for [u ["Kilian" "Kjell" "Kres"]]
                             ^{:key u}
                             [radio-button
                              :label u
                              :value u
                              :model user
                              :on-change   #(reset! user %)]))
                          [gap :size "20px"]
                          [button
                           :label    "Los geht's"
                           :on-click #(rf/dispatch-sync [::events/load-audio @user])]]]])))

; [:pre (with-out-str (pprint @db))]
  
(defn show-db []
  (let [db (rf/subscribe [::subs/db])]
    [:pre 
     (with-out-str (pprint (dissoc @db :re-pressed.core/keydown)))]))

(defn main-panel []
  (dispatch-keydown-rules)
  [v-box
   :height "100vh"
   :children [#_[show-db]
              [start-modal]
              [h-box
               :size "20"
               :children [[h-box
                           :size "1"
                           :justify :center
                           :children [[to-solve]]]
                          [h-box
                           :size "1"
                           :justify :center
                           :align :center
                           :children [[numpad]]]]]
              [show-progress-bar]
              [gap :size "1"]]])

#_(def selected-range (reagent.core/atom "10"))
#_(defn range-select []
  [v-box
   :size "1"
   :children [(doall
               (for [r ["10" "20" "99"]]
                 ^{:key r}
                 [radio-button
                  :label       r
                  :value       r
                  :model       selected-range
                  :on-change   #(reset! selected-range %)]))]])

#_(defonce selected-plus (reagent.core/atom false))
#_(defonce selected-minus (reagent.core/atom false))
#_(defonce selected-times (reagent.core/atom false))
#_(defonce selected-divide (reagent.core/atom false))

#_(defn ops-select []
  [v-box
   :size "1"
   :children [[checkbox
               :label       '+
               :model       selected-plus
               :disabled?   (or @selected-times @selected-divide)
               :on-change   #(reset! selected-plus %)]
              [checkbox
               :label       '-
               :model       selected-minus
               :disabled?   (or @selected-times @selected-divide)
               :on-change   #(reset! selected-minus %)]
              [checkbox
               :label       '*
               :model       selected-times
               :disabled?   (or @selected-plus @selected-minus)
               :on-change   #(reset! selected-times %)]
              [checkbox
               :label       '/
               :model       selected-divide
               :disabled?   (or @selected-plus @selected-minus)
               :on-change   #(reset! selected-divide %)]]])


#_(defn main-panel []
  [v-box
   :height "100vh"
   :justify :center
   :children [[h-box
               :align :center
               :children [[gap :size "5"]
                          [range-select]
                          [gap :size "1"]
                          [ops-select]
                          [gap :size "5"]]
               ]]])
