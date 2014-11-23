(ns jekker.core-test
  (:require [clojure.test :refer :all]
            [jekker.core :refer :all]))

(def template "{\"key\":\"val\"}")
(def json "{\"key\":\"some value\"}")

(deftest matching-keys
  (testing "json keys should match template"
    (is (check template json))))

(deftest test (is '(true false)))
