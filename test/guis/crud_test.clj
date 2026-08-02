(ns guis.crud-test
  (:require [clojure.test :refer [deftest is testing]]
            [guis.crud :as crud]
            [guis.test-data :as test-data]
            [lookup.core :as lookup]))

(deftest render-test
  (testing "Renders people sorted by name"
    (is (= (->> (crud/render-ui
                 {::crud/people
                  test-data/people-of-crud})
                (lookup/select :li.list-row)
                (map lookup/text))
           ["Emil, Hans"
            "Mustermann, Adriana"
            "Mustermann, Max"
            "Tisch, Roman"])))

  (testing "Filters people by family name"
    (is (= (->> (crud/render-ui
                 {::crud/family-name-filter "mus"
                  ::crud/people test-data/people-of-crud})
                (lookup/select :li.list-row)
                (map lookup/text))
           ["Mustermann, Adriana"
            "Mustermann, Max"])))

  (testing "Filters people by upper case family name"
    (is (= (->> (crud/render-ui
                 {::crud/family-name-filter "MUS"
                  ::crud/people test-data/people-of-crud})
                (lookup/select :li.list-row)
                (map lookup/text))
           ["Mustermann, Adriana"
            "Mustermann, Max"])))

  (testing "Blank filter shows everyone"
    (is (= (->> (crud/render-ui
                 {::crud/family-name-filter "  "
                  ::crud/people test-data/people-of-crud})
                (lookup/select :li.list-row)
                (map lookup/text))
           ["Emil, Hans"
            "Mustermann, Adriana"
            "Mustermann, Max"
            "Tisch, Roman"])))

  (testing "Sets family name filter"
    (is (= (->> (crud/render-ui {::crud/people test-data/people-of-crud})
                (lookup/select-one ::crud/input)
                lookup/attrs
                :on :input)
           [[::crud/set-family-name-filter [:event.target/value]]]))))
