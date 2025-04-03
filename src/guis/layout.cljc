(ns guis.layout)

(defn tab-bar [current-view views]
  [:div.tabs.tabs-box.mb-4 {:role "tablist"}
   (for [{:keys [id text]} views]
     (let [current? (= id current-view)]
       [:a.tab (cond-> {:role "tab"}
                 current? (assoc :class "tab-active")
                 (not current?)
                 (assoc-in [:on :click]
                           [[:action/assoc-in [:current-view] id]]))
        text]))])
