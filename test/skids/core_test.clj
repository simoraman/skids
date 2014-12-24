(ns skids.core-test
  (:require [clojure.test :refer :all]
            [skids.core :refer :all]))

(deftest matching-keys
  (testing "json keys should match template"
    (let [template "{\"key\":\"val\"}"
          json "{\"key\":\"some value\"}"]
      (is (:valid (check template json))))))

(deftest additional-data
  (testing "json may have more data than template"
    (let [template "{\"key\":\"val\"}"
          json "{\"key\":\"some value\", \"foo\":\"some value\"}"]
      (is (:valid (check template json))))))

(deftest non-matching-keys
  (testing "non-matching keys should fail"
    (let [template "{\"key\":\"val\"}"
          json "{\"foo\":\"some value\"}"]
      (is (not (:valid (check template json)))))
    (let [template "{\"foo\":\"some value\", \"key\":\"val\"}"
          json "{\"foo\":\"some value\"}"]
      (is (not (:valid (check template json)))))))

(deftest order
  (testing "order should not matter"
    (let [template "{\"key\":\"val\", \"foo\":\"bar\"}"
          json "{\"foo\":\"some value\", \"key\":\"val\"}"]
      (is (:valid (check template json))))))

(deftest number
  (testing "numeric value"
    (let [template "{\"key\":123}"
          json "{\"key\":1}"]
      (is (:valid (check template json))))
    (let [template "{\"key\":123}"
          json "{\"key\":\"1\"}"]
      (is (not (:valid (check template json)))))))

(deftest string
  (testing "string"
    (let [template "{\"key\":\"string\"}"
          json "{\"key\":\"1\"}"]
      (is (:valid (check template json))))
    (let [template "{\"key\":\"string\"}"
          json "{\"key\":1}"]
      (is (not (:valid (check template json)))))))

(deftest array
  (testing "array"
    (let [template "{\"key\":[3,2,1]}"
          json "{\"key\":[1,2,3]}"]
      (is (:valid (check template json))))
    (let [template "{\"key\":[1,2,3]}"
          json "{\"key\":1}"]
      (is (not (:valid (check template json)))))))

(deftest objects-in-array
  (testing "objects in array should be checked"
    (let [template "{\"key\":[{\"foo\":123}]}"
          json "{\"key\":[{\"foo\":321}]}"
          invalid-json "{\"key\":[{\"foo\":\"321\"}]}"]
      (is (:valid (check template json)))
      (is (not (:valid (check template invalid-json)))))))

(deftest all-items-in-array
  (testing "all items in array should be checked"
    (let [template "{\"key\":[{\"foo\":123}]}"
          json "{\"key\":[{\"foo\":321},{\"foo\":123}]}"
          invalid-json "{\"key\":[{\"foo\":321},{\"foo\":\"321\"}]}"]
      (is (:valid (check template json)))
      (is (not (:valid (check template invalid-json)))))))

(deftest boolean-test
  (testing "boolean"
    (let [template "{\"key\":true}"
          json "{\"key\":true}"]
      (is (:valid (check template json))))
    (let [template "{\"key\":true}"
          json "{\"key\":\"true\"}"]
      (is (not (:valid (check template json)))))))

(deftest object
  (testing "object"
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\": { \"foo\":123 }}"]
      (is (:valid (check template json))))
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\":\"foo\"}"]
      (is (not (:valid (check template json))))))
  (testing "object structure must be correct"
    (let [template "{\"key\":{ \"foo\":123 }}"
          json "{\"key\":{ \"foo\":true }}"]
      (is (not (:valid (check template json)))))))

(testing "Reporting"
  (testing "Should report missing key"
    (deftest missing-key
      (let [template "{\"key\":\"val\"}"
            json "{\"key\":\"some value\"}"
            result (check template json)]
        (is (= (:message result) "key key not found"))))))
(run-tests)
