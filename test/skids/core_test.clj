(ns skids.core-test
  (:require [clojure.test :refer :all]
            [skids.core :refer :all]))

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
    (let [template "{\"key\":123}"
          json "{\"key\":1}"]
      (is (check template json)))
    (let [template "{\"key\":123}"
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
    (let [template "{\"key\":[3,2,1]}"
          json "{\"key\":[1,2,3]}"]
      (is (check template json)))
    (let [template "{\"key\":[1,2,3]}"
          json "{\"key\":1}"]
      (is (not (check template json))))))

(deftest objects-in-array
  (testing "objects in array should be checked"
    (let [template "{\"key\":[{\"foo\":123}]}"
          json "{\"key\":[{\"foo\":321}]}"
          invalid-json "{\"key\":[{\"foo\":\"321\"}]}"]
      (is (check template json))
      (is (not (check template invalid-json))))))

(deftest all-items-in-array
  (testing "all items in array should be checked"
    (let [template "{\"key\":[{\"foo\":123}]}"
          json "{\"key\":[{\"foo\":321},{\"foo\":123}]}"
          invalid-json "{\"key\":[{\"foo\":321},{\"foo\":\"321\"}]}"]
      (is (check template json))
      (is (not (check template invalid-json))))))

(deftest boolean-test
  (testing "boolean"
    (let [template "{\"key\":true}"
          json "{\"key\":true}"]
      (is (check template json)))
    (let [template "{\"key\":true}"
          json "{\"key\":\"true\"}"]
      (is (not (check template json))))))

(deftest object
  (testing "object"
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\": { \"foo\":123 }}"]
      (is (check template json)))
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\":\"foo\"}"]
      (is (not (check template json)))))
  (testing "object structure must be correct"
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\":{ \"foo\":true }}"]
      (is (not (check template json))))))

(run-tests)
