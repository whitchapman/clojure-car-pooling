(ns clojure-car-pooling.handlers-test
  (:require [clojure.test :refer :all]
            [clojure-car-pooling.handlers :refer :all]))

(deftest lookup-test
  (let [data [{:link "http://www.samferda.net/en/detail/103630"}
              {:link "http://www.samferda.net/en/detail/103632"}]]
    (testing "successful lookups"
      (is (not= nil (lookup-by-id data "103630")))
      (is (not= nil (lookup-by-id data "103632"))))

    (testing "unsuccessful lookups"
      (is (= nil (lookup-by-id data "103631")))
      (is (= nil (lookup-by-id data 103630))))))
