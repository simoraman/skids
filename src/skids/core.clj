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
      (every? (fn [x] (true? (:valid x))) (map (partial matches (first matcher)) val))
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

(defn true-maybe? [x]
  (or (true? x) (true? (:valid x))))
(defn values-match? [template json]
  (let [resp (every? true-maybe? (map (fn [x]
                                        (value-match? (get template (key x)) (val x)))
                                json))]
    (create-response resp)))

(defn- key-match? [json key-to-test]
  (cond
   (in? (keys json) key-to-test) (create-response true)
   :else (create-response false (str "key " (name key-to-test) " not found"))))

(defn keys-match? [template json]
  (first (filter (fn [x] (not (:valid x))) (map #(key-match? json %1) (keys template)))))

(defn- matches [template json]
  (let [keys-ok? (keys-match? template json)]
    (cond
     (empty? keys-ok?) (values-match? template json)
     :else keys-ok?)))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        m (matches template json)]
    m))
