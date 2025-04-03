(ns guis.dev
  (:require [guis.core :as guis]))

(def store (atom {:number 0}))

(defn main []
  (guis/init store)
  (println "Loaded!"))

(defn ^:dev/after-load reload []
  (guis/init store)
  (println "Reloaded!!"))
