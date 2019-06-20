(ns clojure-car-pooling.handlers
  (:require [clojure.pprint :refer [pprint]]
            [clojure-car-pooling.regex :as regex]
            [cemerick.url :refer [url-encode]]
            [clj-http.client :as http]
            [environ.core :refer [env]]))

(defn- add-additional-fields-for-list [{:keys [link] :as data}]
  (let [id (regex/lookup-id link)]
    (assoc data :id id)))

(defn- lookup-distance [mapquest-key {:keys [to from]}]
  (let [uri (str "https://www.mapquestapi.com/directions/v2/route?key="
                 mapquest-key
                 "&unit=k&from="
                 (url-encode (str from ", Iceland"))
                 "&to="
                 (url-encode (str to ", Iceland"))
                 "&outFormat=json&ambiguities=ignore&routeType=fastest"
                 "&doReverseGeocode=false&enhancedNarrative=false"
                 "&avoidTimedConditions=false")
        ;;_ (println "URI> " uri)
        body (:body (http/get uri {:throw-exceptions false :as :json}))]
    ;;(println "BODY> " body)
    (:distance body)))

(defn- add-additional-fields-for-get [{:keys [link] :as data}]
  (println "Querying link> " link)
  (let [text (:body (http/get link {:throw-exceptions false}))
        data (reduce (fn [data key] (assoc data key (regex/lookup-field text key)))
                     data [:seats :name :phone :mobile :email :non-smoke-car])]
    (if-let [mapquest-key (:mapquest-key env)]
      (do
        ;;(println "MAPQUEST_KEY> " mapquest-key)
        (let [distance (lookup-distance mapquest-key data)]
          ;;(println "DISTANCE> " distance)
          (assoc data :distance distance)))
      data)))

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

      (println "Driver ID> " id)

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

      (println "Passenger ID> " id)

      (if-let [data (lookup-by-id (:results (:body raw)) id)]

        {:status 200
         ;;:headers {"Content-Type" "application/json;charset=UTF-8"}
         :body (add-additional-fields-for-get data)}

        {:status 404}))

    (catch Exception exn
      (pprint "EXN>" exn)
      (throw exn))))
