(ns guis.core
  (:require [guis.counter :as counter]
            [replicant.dom :as r]))

(defn render-ui [state]
  (r/render
   js/document.body
   (counter/render-ui state)))

(defn init [store]
  (add-watch store ::render (fn [_ _ _ new-state]
                              (render-ui new-state)))

  (r/set-dispatch!
   (fn [_ event-data]
     (doseq [[action & args] event-data]
       (case action
         ::counter/inc-number
         (swap! store update :number inc)))))

  (swap! store assoc ::loaded-at (.getTime (js/Date.))))
