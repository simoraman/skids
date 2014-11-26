(ns chromedome.core
  (:require [cheshire.core :refer :all]))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn value-match [matcher val]
  (cond
   (= matcher "number" ) (number? val)
   :else true))

(defn matches [template json]
  (cond
   (not-every? true? (map #(in? (keys json) %1) (keys template))) false
   :else (every? true? (map (fn [x] (value-match (get template (key x)) (val x))) json))))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        key-matches (matches template json)]
    key-matches))
