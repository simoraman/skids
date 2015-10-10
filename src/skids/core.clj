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

(defn match [template [k v]]
  (let [current (get template k)]
    (if current
      (cond
        (boolean? v) (if (boolean? current)
                       (create-response true)
                       (create-response false))
        (number? v)
        (if (number? current)
          (create-response true)
          (create-response false (str (name k) " is not a number")))
        (string? v)
        (if (string? current)
          (create-response true)
          (create-response false (str (name k) " is not a string")))
        (vector? v)
        (if (vector? current)
          (create-response true)
          (create-response false (str (name k) " is not an array")))
        (map? v)
        (if (map? current)
          (map #(match current %) v)
          (create-response false (str (name k) " is not an object")))
        :else (create-response true))
      (create-response false (str "key " (name k) " not found")))))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        match-template (partial match json)
        falses (filter #(= false (:valid %)) (map #(match-template %) template))]
    (if (empty? falses) (create-response true) (first falses))))
