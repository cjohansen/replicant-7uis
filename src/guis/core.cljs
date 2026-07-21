(ns guis.core
  (:require [guis.counter :as counter]
            [guis.flights :as flights]
            [guis.layout :as layout]
            [guis.temperature :as temperature]
            [guis.timer :as timer]
            [nexus.core :as nexus]
            [replicant.dom :as r]))

(def views
  [{:id :counter
    :text "Counter"}
   {:id :temperatures
    :text "Temperatures"}
   {:id :flights
    :text "Flights"}
   {:id :timer
    :text "Timer"
    :on-load-actions timer/on-load}])

(def id->view (into {} (map (juxt :id identity) views)))

(defn get-current-view [state]
  (:current-view state))

(defn render-ui [state]
  (let [current-view (get-current-view state)]
    [:div.m-8
     (layout/tab-bar current-view views)
     (case current-view
       :counter
       (counter/render-ui state)

       :flights
       (flights/render-ui state)

       :temperatures
       (temperature/render-ui state)

       :timer
       (timer/render-ui state)

       [:h1.text-lg "Select your UI of choice"])]))

(def nexus
  {:nexus/system->state
   (fn [store]
     (assoc @store :now (js/Date.)))

   :nexus/effects
   {:effect/assoc-in
    (fn [_ store path v]
      (swap! store assoc-in path v))

    :effect/schedule
    (fn [{:keys [dispatch]} _ ms actions]
      (js/setTimeout #(dispatch actions) ms))}

   :nexus/actions
   (merge counter/actions
          temperature/actions
          timer/actions)

   :nexus/placeholders
   {:event.target/value
    (fn [{:keys [event]}]
      (some-> event .-target .-value))

    :fmt/number
    (fn [_ val]
      (some-> val parse-long))

    :fmt/keyword
    (fn [_ val]
      (some-> val keyword))

    :clock/now (fn [_] (js/Date.))}})

(defn trigger-on-load [nexus store old-state new-state]
  (let [new-view (get-current-view new-state)]
    (when-not (= (get-current-view old-state) new-view)
      (when-let [actions (get-in id->view [new-view :on-load-actions])]
        (nexus/dispatch nexus store nil actions)))))

(defn init [nexus store]
  (add-watch store ::render (fn [_ _ old-state new-state]
                              (trigger-on-load nexus store old-state new-state)
                              (r/render
                               js/document.body
                               (render-ui (assoc new-state :now (js/Date.))))))

  (r/set-dispatch!
   (fn [{:replicant/keys [dom-event]} actions]
     (nexus/dispatch nexus store {:event dom-event} actions)))

  (swap! store assoc ::loaded-at (.getTime (js/Date.))))
