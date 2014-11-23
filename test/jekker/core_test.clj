(ns jekker.core-test
  (:require [clojure.test :refer :all]
            [jekker.core :refer :all]))

(deftest matching-keys
  (testing "json keys should match template"
    (let [template "{\"key\":\"val\"}"
          json "{\"key\":\"some value\"}"]
      (is (check template json)))))

(deftest non-matching-keys
  (testing "non-matching keys should fail"
    (let [template "{\"key\":\"val\"}"
          json "{\"foo\":\"some value\"}"]
      (is (not (check template json))))))

(deftest order
  (testing "order should not matter"
    (let [template "{\"key\":\"val\", \"foo\":\"bar\"}"
          json "{\"foo\":\"some value\", \"key\":\"val\"}"]
      (is (check template json)))))
