(ns guis.dev
  (:require [dataspex.core :as dataspex]
            [guis.core :as guis]))

(defonce store (atom {:number 0}))
(dataspex/inspect "App state" store)

(defn main []
  (guis/init store)
  (println "Loaded!"))

(defn ^:dev/after-load reload []
  (guis/init store)
  (println "Reloaded!!"))
