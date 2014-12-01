(ns chromedome.core-test
  (:require [clojure.test :refer :all]
            [chromedome.core :refer :all]))

(deftest matching-keys
  (testing "json keys should match template"
    (let [template "{\"key\":\"val\"}"
          json "{\"key\":\"some value\"}"]
      (is (check template json)))))

(deftest additional-data
  (testing "json may have more data than template"
    (let [template "{\"key\":\"val\"}"
          json "{\"key\":\"some value\", \"foo\":\"some value\"}"]
      (is (check template json)))))

(deftest non-matching-keys
  (testing "non-matching keys should fail"
    (let [template "{\"key\":\"val\"}"
          json "{\"foo\":\"some value\"}"]
      (is (not (check template json))))
    (let [template "{\"foo\":\"some value\", \"key\":\"val\"}"
          json "{\"foo\":\"some value\"}"]
      (is (not (check template json))))))

(deftest order
  (testing "order should not matter"
    (let [template "{\"key\":\"val\", \"foo\":\"bar\"}"
          json "{\"foo\":\"some value\", \"key\":\"val\"}"]
      (is (check template json)))))

(deftest number
  (testing "numeric value"
    (let [template "{\"key\":\"number\"}"
          json "{\"key\":1}"]
      (is (check template json)))
    (let [template "{\"key\":\"number\"}"
          json "{\"key\":\"1\"}"]
      (is (not (check template json))))))

(deftest string
  (testing "string"
    (let [template "{\"key\":\"string\"}"
          json "{\"key\":\"1\"}"]
      (is (check template json)))
    (let [template "{\"key\":\"string\"}"
          json "{\"key\":1}"]
      (is (not (check template json))))))

(deftest array
  (testing "array"
    (let [template "{\"key\":\"array\"}"
          json "{\"key\":[1,2,3]}"]
      (is (check template json)))
    (let [template "{\"key\":\"array\"}"
          json "{\"key\":1}"]
      (is (not (check template json))))))

(deftest boolean
  (testing "boolean"
    (let [template "{\"key\":\"boolean\"}"
          json "{\"key\":true}"]
      (is (check template json)))
    (let [template "{\"key\":\"boolean\"}"
          json "{\"key\":\"true\"}"]
      (is (not (check template json))))))

(deftest object
  (testing "object"
    (let [template "{\"key\":\"object\"}"
          json "{\"key\": { \"foo\":123 }}"]
      (is (check template json)))
    (let [template "{\"key\":\"object\"}"
          json "{\"key\":\"foo\"}"]
      (is (not (check template json))))))
