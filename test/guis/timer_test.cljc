(ns guis.timer-test
  (:require [clojure.test :refer [deftest testing is]]
            [guis.timer :as timer]))

(deftest get-view-state-test
  (testing "Uses sane defaults"
    (is (= (timer/get-view-state {})
           {:pct 0
            :elapsed "0s"
            :duration 20})))

  (testing "Uses set duration"
    (is (= (timer/get-view-state {::timer/duration 15})
           {:pct 0
            :elapsed "0s"
            :duration 15})))

  (testing "Calculates elapsed from started"
    (is (= (timer/get-view-state {:now #inst "2025-06-02T21:00:05"
                                  ::timer/started #inst "2025-06-02T21:00:00"
                                  ::timer/duration 10})
           {:pct 50
            :elapsed "5s"
            :duration 10})))

  (testing "Calculates elapsed in tenths of a second"
    (is (= (timer/get-view-state {:now #inst "2025-06-02T21:00:14.031"
                                  ::timer/started #inst "2025-06-02T21:00:00"
                                  ::timer/duration 20})
           {:pct 70
            :elapsed "14s"
            :duration 20})))

  (testing "Stops when elapsed = duration"
    (is (= (timer/get-view-state {:now #inst "2025-06-02T21:01:05.123"
                                  ::timer/started #inst "2025-06-02T21:00:00.456"
                                  ::timer/duration 10})
           {:pct 100
            :elapsed "10s"
            :duration 10}))))
