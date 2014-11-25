(ns chromedome.core
  (:require [cheshire.core :refer :all]))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)
        key-matches (map #(in? (keys json) %1) (keys template))]
    (not (or (in? key-matches false) (in? key-matches nil)))))
