(ns jekker.core
  (:require [cheshire.core :refer :all]))

(defn check [template-string json-string]
  (let [template (parse-string template-string true)
        json (parse-string json-string true)]
    (map = (keys template) (keys json))))
