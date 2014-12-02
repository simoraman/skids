(ns chromedome.core
  (:require [cheshire.core :refer :all]))

(defn- in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn- boolean? [val]
  (or (= true val) (= false val)))

(defn- value-match [matcher val]
  (cond
   (boolean? matcher) (boolean? val)
   (vector? matcher) (vector? val)
   (number? matcher) (number? val)
   (string? matcher) (string? val)
   (map? matcher) (map? val)
   :else true))

(defn- keys-match? [template json]
  (every? true? (map #(in? (keys json) %1) (keys template))))

(defn- matches [template json]
  (cond
   (not (keys-match? template json)) false
   :else (every? true? (map (fn [x] (value-match (get template (key x)) (val x))) json))))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        key-matches (matches template json)]
    key-matches))
