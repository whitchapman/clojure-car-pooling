(ns clojure-car-pooling.core-test
  (:require [clojure.test :refer :all]
            [clojure-car-pooling.core :refer :all]))

(deftest routing-tests
  (testing "redirect"
    (is (= (app {:request-method :get :uri "/"})
           {:status 302
            :headers {"Location" "/index.html"}
            :body ""})))

  (testing "not found"
    (is (= (app {:request-method :get :uri "/rides"})
           {:status 404
            :body ""
            :headers {}})))

  ;;these tests have external dependencies that make them impractical for
  ;;TODO: provide overrides for http calls so can test return values
  #_(testing "valid routes"
    (let [resp (app {:request-method :get :uri "/rides/samferda-drivers/"})]
      (is (= 200 (:status resp))))

    (let [resp (app {:request-method :get :uri "/rides/samferda-passengers/"})]
      (is (= 200 (:status resp))))))
