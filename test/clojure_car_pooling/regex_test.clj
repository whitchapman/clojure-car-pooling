(ns clojure-car-pooling.regex-test
  (:require [clojure.test :refer :all]
            [clojure-car-pooling.regex :refer :all]))

(deftest field-tests
  (let [text "<tr>
<td align=\"right\"><b>Seats:</b></td>
<td>1</td>
</tr>
<tr>
<td align=\"right\"><b>Name:</b></td>
<td>Giovanni</td>
</tr>
<tr>
<td align=\"right\"><b>Phone:</b></td>
<td>+393924501719</td>
</t
r>
<tr>
<td align=\"right\"><b>Mobile:</b></td>
<td>+393924501719</td>
</tr>
<tr>
<td align=\"right\"><b>E-mail:</b></td>
<td>giovannitorre.89@gmail.com</td>
</tr>
<tr>
<td align=\"right\"><b>Non-smoke car:</b></td>
<td>yes</td>
</tr>
"]

    (testing "Seats"
      (is (= "1" (lookup-field text :seats))))

    (testing "Name"
      (is (= "Giovanni" (lookup-field text :name))))

    (testing "Phone"
      (is (= "+393924501719" (lookup-field text :phone))))

    (testing "Mobile"
      (is (= "+393924501719" (lookup-field text :mobile))))

    (testing "E-mail"
      (is (= "giovannitorre.89@gmail.com" (lookup-field text :email))))

    (testing "Non-smoke car"
      (is (= "yes" (lookup-field text :non-smoke-car))))))

(deftest id-tests
    (testing "successful test"
      (is (= "103630" (lookup-id "http://www.samferda.net/en/detail/103630"))))

    (testing "unsuccessful test"
      (is (= nil (lookup-id "http://www.samferda.net/en/detail/")))))
