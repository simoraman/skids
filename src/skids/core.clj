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
  (if (get template k)
    (cond
      (boolean? v) (if (boolean? (get template k))
                     (create-response true)
                     (create-response false))
      (number? v)
      (if (number? (get template k))
        (create-response true)
        (create-response false (str (name k) " is not a number")))
      (string? v)
      (if (string? (get template k))
        (create-response true)
        (create-response false (str (name k) " is not a string")))
      (vector? v)
      (if (vector? (get template k))
        (create-response true)
        (create-response false (str (name k) " is not an array")))
      (map? v)
      (if (map? (get template k))
        (map #(match (get template k) %) v)
        (create-response false (str (name k) " is not an array")))
      :else (create-response true))
    (create-response false (str "key " (name k) " not found"))))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        match-template (partial match json)
        falses (filter #(= false (:valid %)) (map #(match-template %) template))]
    (if (empty? falses) (create-response true) (first falses))))
