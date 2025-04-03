(ns guis.counter)

(defn render-ui [state]
  [:div.m-8
   [:h1.text-lg "Counter"]
   [:div.flex.gap-4.items-center
    [:div "Number is " (:number state)]
    [:button.btn
     {:on {:click [[::inc-number]]}}
     "Count!"]]])
