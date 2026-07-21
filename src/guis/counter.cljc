(ns guis.counter)

(defn render-ui [state]
  [:div
   [:h1.text-lg "Counter"]
   [:div.flex.gap-4.items-center
    [:div "Number is " (:number state)]
    [:button.btn
     {:on {:click [[::inc-number]]}}
     "Count!"]]])

(def view
  {:id :counter
   :text "Counter"
   :render #'render-ui
   :actions
   {::inc-number
    (fn [state]
      [[:effect/assoc-in [:number] (inc (:number state))]])}})
