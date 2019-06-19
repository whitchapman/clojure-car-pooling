(ns clojure-car-pooling.handlers
  (:require [clojure.pprint :refer [pprint]]
            [clj-http.client :as http]))

(defn- add-additional-fields [{:keys [link] :as data}]
  (println "Querying link> " link)
  (let [raw (http/get link {:throw-exceptions false})]
    ;;(println raw)
    data))

(defn drivers-handler [_]
  (try
    (let [raw (http/get "https://apis.is/rides/samferda-drivers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Drivers> " (count (:results (:body raw))))

      (let [first-result (first (:results (:body raw)))]
        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields first-result)}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))

(defn passengers-handler [_]
  (try
    (let [raw (http/get "https://apis.is/rides/samferda-passengers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Passengers> " (count (:results (:body raw))))

      (let [first-result (first (:results (:body raw)))]
        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields first-result)}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))
