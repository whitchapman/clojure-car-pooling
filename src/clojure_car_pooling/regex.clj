(ns clojure-car-pooling.regex
  (:require [clojure.pprint :refer [pprint]]))

(def regex-map {:seats #"Seats:</b></td>\s*<td>([^<]*)</td>"
                :name #"Name:</b></td>\s*<td>([^<]*)</td>"
                :phone #"Phone:</b></td>\s*<td>([^<]*)</td>"
                :mobile #"Mobile:</b></td>\s*<td>([^<]*)</td>"
                :email #"E-mail:</b></td>\s*<td>([^<]*)</td>"
                :non-smoke-car #"Non-smoke car:</b></td>\s*<td>([^<]*)</td>"})

(defn lookup-field [text field]
  (second (re-find (get regex-map field) text)))

(defn lookup-id [link]
  (second (re-find #"/(\d+)$" link)))
