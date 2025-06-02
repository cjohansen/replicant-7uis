(ns guis.timer)

(defn get-time [inst]
  (.getTime inst))

(defn format-seconds [s]
  (let [s10 (int (* 10 s))]
    (if (= 0 (mod s10 10))
      (int (/ s10 10))
      (float (/ s10 10)))))

(defn get-view-state [state]
  (let [duration (or (::duration state) 20)
        elapsed (min (if-let [started (::started state)]
                       (/ (- (get-time (:now state)) (get-time started)) 1000)
                       0)
                     duration)]
    {:pct (int (* 100 (/ elapsed duration)))
     :elapsed (str (format-seconds elapsed) "s")
     :duration duration}))

(defn label [s]
  [:span.w-30.shrink-0 s])

(defn render-ui [state]
  (let [{:keys [pct duration elapsed]} (get-view-state state)]
    [:div.max-w-96.flex.flex-col.gap-4
     [:h1.text-lg "Timer"]
     [:div.flex.items-center
      (label "Elapsed time: ")
      [:progress.progress {:value pct :max 100}]]
     (label elapsed)
     [:div.flex.items-center
      (label "Duration: ")
      [:input.range
       {:type "range"
        :min 0
        :max 100
        :value duration
        :on {:input [[:action/assoc-in [::duration] :event.target/value-as-number]]}}]]
     [:button.btn 
      {:on {:click [[:action/assoc-in [::started] :clock/now]]}}
      "Reset"]
     [:button.btn
      {:on {:click [[:action/assoc-in [::last-tick] :clock/now]]}}
      "Tick!"]]))
