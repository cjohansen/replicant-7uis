(ns guis.counter)

(defn perform-action [state [action]]
  (when (= ::inc-number action)
    [[:effect/assoc-in [:number] (inc (:number state))]]))

(defn render-ui [state]
  [:div
   [:h1.text-lg "Counter"]
   [:div.flex.gap-4.items-center
    [:div "Number is " (:number state)]
    [:button.btn
     {:on {:click [[::inc-number]]}}
     "Count!"]]])
