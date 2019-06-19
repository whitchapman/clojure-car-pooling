(ns clojure-car-pooling.handlers
  (:require [clojure.pprint :refer [pprint]]
            [clojure-car-pooling.regex :as regex]
            [clj-http.client :as http]))

(defn- add-additional-fields-for-list [{:keys [link] :as data}]
  (let [id (regex/lookup-id link)]
    (assoc data :id id)))

(defn- add-additional-fields-for-get [{:keys [link] :as data}]
  (println "Querying link> " link)
  (let [text (:body (http/get link {:throw-exceptions false}))]
    (reduce (fn [data key] (assoc data key (regex/lookup-field text key)))
            (add-additional-fields-for-list data)
            [:seats :name :phone :mobile :email :non-smoke-car])))

(defn list-drivers [_]
  (try
    (let [raw (http/get "https://apis.is/rides/samferda-drivers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Drivers> " (count (:results (:body raw))))

      {:status 200
       ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
       :body (->> (:results (:body raw))
                  (mapv add-additional-fields-for-list))})

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))

(defn get-drivers [id]
  (try
    (println "ID> " id)
    (let [raw (http/get "https://apis.is/rides/samferda-drivers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Drivers> " (count (:results (:body raw))))

      (let [first-result (first (:results (:body raw)))]
        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields-for-get first-result)}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))

(defn list-passengers [_]
  (try
    (let [raw (http/get "https://apis.is/rides/samferda-passengers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Passengers> " (count (:results (:body raw))))

      {:status 200
       ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
       :body (->> (:results (:body raw))
                  (mapv add-additional-fields-for-list))})

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))

(defn get-passenger [id]
  (try
    (println "ID> " id)
    (let [raw (http/get "https://apis.is/rides/samferda-passengers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Passengers> " (count (:results (:body raw))))

      (let [first-result (first (:results (:body raw)))]
        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields-for-get first-result)}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))
