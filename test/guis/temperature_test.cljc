(ns guis.temperature-test
  (:require [clojure.test :refer [deftest testing is]]
            [guis.temperature :as temperature]))

(deftest fahrenheit->celsius
  (testing "Converts to Celsius"
    (is (= (temperature/fahrenheit->celsius 32) 0))
    (is (= (temperature/fahrenheit->celsius 122) 50))
    (is (= (temperature/fahrenheit->celsius 212) 100))))

(deftest celsius->fahrenheit
  (testing "Converts to Fahrenheit"
    (is (= (temperature/celsius->fahrenheit 0) 32))
    (is (= (temperature/celsius->fahrenheit 50) 122))
    (is (= (temperature/celsius->fahrenheit 100) 212))))

(deftest set-temperature
  (testing "Sets temperature and calculates Fahrenheit"
    (is (= (temperature/set-temperature {:celsius 50})
           [[:effect/assoc-in [:celsius] 50]
            [:effect/assoc-in [:fahrenheit] 122]])))

  (testing "Sets temperature and calculates Celsius"
    (is (= (temperature/set-temperature {:fahrenheit 212})
           [[:effect/assoc-in [:celsius] 100]
            [:effect/assoc-in [:fahrenheit] 212]]))))
