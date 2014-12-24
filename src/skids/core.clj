(ns skids.core
  (:require [cheshire.core :refer :all]))

(defn- in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn- boolean? [val]
  (or (= true val) (= false val)))

(defn- create-response
  ([valid] {:valid valid})
  ([valid message] {:valid valid :message message}))

(declare matches)

;;TODO should check all values in the target json
(defn check-array [matcher val]
  (if (vector? val)
    (if (map? (first val))
      (every? true? (map (partial matches (first matcher)) val))
      true)
    false))

(defn- value-match? [matcher val]
  (cond
   (boolean? matcher) (boolean? val)
   (vector? matcher) (check-array matcher val)
   (number? matcher) (number? val)
   (string? matcher) (string? val)
   (map? matcher) (if (map? val) (matches matcher val) false)
   :else true))

(defn- key-match? [json key-to-test]
  (cond
   (in? (keys json) key-to-test) true
   :else false))
(defn keys-match? [template json]
  (every? true? (map #(key-match? json %1) (keys template))))

(defn values-match? [template json]
  (every? true? (map (fn [x]
                       (value-match? (get template (key x)) (val x)))
                     json)))

(defn- matches [template json]
  (cond
   (not (keys-match? template json)) false
   :else (values-match? template json)))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)]
    {:valid (matches template json) :message "key key not found"}))
