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

(defn matches [template json]
  (let [has-keys? (map #(in? (keys json) %) (keys template))]
    (every? true? has-keys?)))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)]
    {:valid (matches template json)}))
