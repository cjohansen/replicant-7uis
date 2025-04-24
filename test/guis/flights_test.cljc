(ns guis.flights-test
  (:require [clojure.test :refer [deftest testing is]]
            [guis.flights :as flights]))

(deftest get-form-state-test
  (testing "Defaults to one-way flight"
    (is (= (-> (flights/get-form-state {})
               ::flights/type)
           :one-way)))

  (testing "Uses selected flight type"
    (is (= (-> {::flights/type :roundtrip}
               flights/get-form-state
               ::flights/type)
           :roundtrip)))

  (testing "Defaults to today for departure date"
    (is (= (-> (flights/get-form-state {:now #inst "2025-04-24"})
               ::flights/departure-date
               :value)
           "2025-04-24")))

  (testing "Uses entered departure date"
    (is (= (-> {::flights/departure-date "2025-07-01"}
               flights/get-form-state
               ::flights/departure-date
               :value)
           "2025-07-01")))

  (testing "Marks invalid departure date"
    (is (true? (-> {::flights/departure-date "2025-07"}
                   flights/get-form-state
                   ::flights/departure-date
                   :invalid?))))

  (testing "Disables button when departure date is invalid"
    (is (true? (-> {::flights/departure-date "2025-07"}
                   flights/get-form-state
                   ::flights/button
                   :disabled?))))

  (testing "Defaults to today for return date"
    (is (= (-> (flights/get-form-state {:now #inst "2025-04-24"})
               ::flights/return-date
               :value)
           "2025-04-24")))

  (testing "Defaults return date to selected departure date"
    (is (= (-> {:now #inst "2025-04-24"
                ::flights/departure-date "2025-07-01"}
               flights/get-form-state
               ::flights/return-date
               :value)
           "2025-07-01")))

  (testing "Uses entered return date"
    (is (= (-> {::flights/return-date "2025-07-01"}
               flights/get-form-state
               ::flights/return-date
               :value)
           "2025-07-01")))

  (testing "Defaults return date to disabled"
    (is (true? (-> {:now #inst "2025-04-24"}
                   flights/get-form-state
                   ::flights/return-date
                   :disabled?))))

  (testing "Return date is enabled when type is roundtrip"
    (is (false? (-> {:now #inst "2025-04-24"
                     ::flights/type :roundtrip}
                    flights/get-form-state
                    ::flights/return-date
                    :disabled?))))

  (testing "Marks invalid return date"
    (is (true? (-> {::flights/return-date "2025-07"
                    ::flights/type :roundtrip}
                   flights/get-form-state
                   ::flights/return-date
                   :invalid?))))

  (testing "Does not mark invalid return date for one-way flights"
    (is (not (-> {::flights/return-date "2025-07"
                  ::flights/type :one-way}
                 flights/get-form-state
                 ::flights/return-date
                 :invalid?))))

  (testing "Disables button when return date is invalid"
    (is (true? (-> {::flights/return-date "2025-07"
                    ::flights/type :roundtrip}
                   flights/get-form-state
                   ::flights/button
                   :disabled?))))

  (testing "Does not disable button when return date is invalid on one-way flight"
    (is (not (-> {::flights/return-date "2025-07"
                  ::flights/type :one-way}
                 flights/get-form-state
                 ::flights/button
                 :disabled?))))

  (testing "Disables button when return date is before departure date"
    (is (true? (-> {::flights/departure-date "2025-07-02"
                    ::flights/return-date "2025-07-01"
                    ::flights/type :roundtrip}
                   flights/get-form-state
                   ::flights/button
                   :disabled?))))

  (testing "Does not disable button when return date is before departure date for one-way flights"
    (is (not (-> {::flights/departure-date "2025-07-02"
                  ::flights/return-date "2025-07-01"
                  ::flights/type :one-way}
                 flights/get-form-state
                 ::flights/button
                 :disabled?)))))
