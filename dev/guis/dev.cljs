(ns guis.dev
  (:require [dataspex.core :as dataspex]
            [guis.core :as guis]
            [guis.test-data :as test-data]
            [nexus.action-log :as action-log]))

(defn inspect-actions [nexus]
  (let [log (action-log/create-log)]
    (action-log/install-logger nexus log)))

(defonce store (atom {:number 0
                      :guis.crud/people test-data/people-of-crud}))
(dataspex/inspect "App state" store)

(def nexus (inspect-actions guis/nexus))

(defn main []
  (guis/init nexus store)
  (println "Loaded!"))

(defn ^:dev/after-load reload []
  (guis/init nexus store)
  (println "Reloaded!!"))
