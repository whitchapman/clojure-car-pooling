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
            data [:seats :name :phone :mobile :email :non-smoke-car])))

(defn lookup-by-id [results id]
  (loop [results results]
    (when-let [[data & results] (seq results)]
      (let [data-with-id (add-additional-fields-for-list data)]
        (if (= id (:id data-with-id))
          data-with-id
          (recur results))))))

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

(defn get-driver [{:keys [path-params]}]
  (try
    (let [id (:id path-params)
          raw (http/get "https://apis.is/rides/samferda-drivers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Drivers> " (count (:results (:body raw))))

      (if-let [data (lookup-by-id (:results (:body raw)) id)]

        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields-for-get data)}

        {:status 404}))

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

(defn get-passenger [{:keys [path-params]}]
  (try
    (let [id (:id path-params)
          raw (http/get "https://apis.is/rides/samferda-passengers/"
                        {:throw-exceptions false :as :json})]

      (println "# of Passengers> " (count (:results (:body raw))))

      (if-let [data (lookup-by-id (:results (:body raw)) id)]

        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields-for-get data)}

        {:status 404}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))
