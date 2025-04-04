(ns guis.temperature)

(defn fahrenheit->celsius [fahrenheit]
  (* (- fahrenheit 32) (/ 5 9)))

(defn celsius->fahrenheit [celsius]
  (+ (* celsius (/ 9 5)) 32))

(defn render-ui [state]
  [:div
   [:h1.text-lg.mb-4 "Temperature conversions"]
   [:div.flex.gap-8
    [:div.flex.gap-4.items-center
     [:input.input.w-14
      {:type "number"
       :id "celsius"
       :value (:celsius state)
       :on {:input [[::set-temperature
                     {:celsius :event.target/value-as-number}]]}}]
     [:label {:for "celsius"} "Celsius"]]
    [:div.flex.gap-4.items-center
     [:input.input.w-14
      {:type "number"
       :id "fahrenheit"
       :value (:fahrenheit state)
       :on {:input [[::set-temperature
                     {:fahrenheit :event.target/value-as-number}]]}}]
     [:label {:for "fahrenheit"} "Fahrenheit"]]]])

(defn set-temperature [{:keys [celsius fahrenheit]}]
  [[:effect/assoc-in [:celsius] (or celsius (fahrenheit->celsius fahrenheit))]
   [:effect/assoc-in [:fahrenheit] (or fahrenheit (celsius->fahrenheit celsius))]])

(defn perform-action [_ [action & args]]
  (when (= ::set-temperature action)
    (set-temperature (first args))))
