
(ns tests-mongo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.config-args :refer [make-config compact-hash]])
  (:require [crash-screech.mongo-db :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(alias 's 'clojure.spec.alpha)
(alias 'm-d 'crash-screech.mongo-db)




(s/fdef m-d/next-date-time
  :args (s/cat :yyyy-mm   :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs))

(s/fdef m-d/mongolabs-build
  :args (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::my-collection string?
               ::pages-to-check vector?))



(defn mongo-db-specs []
 (if RUN-SPEC-TESTS
    (do
(println "Speccing mongo-db")
      (spec-test/instrument)
      (spec-test/instrument 'next-date-time)
      (spec-test/instrument 'mongolabs-build))))




(deftest unit-mongolabs-build
  (testing "count-string : cccccccccccccccccccccc "
    (let [ db-type  "monger-db"
           the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
          pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least 1}]
                   a-mongo-db       (mongolabs-build the-config  T-DB-TEST-NAME pages-to-check)]
      (console-test "unit-mongolabs-build" "mongo-db")

     (is (function?(:my-delete-table a-mongo-db)))
     (is (function?(:my-purge-table a-mongo-db)))
     (is (function?(:my-get-all a-mongo-db)))
     (is (function?(:my-get-url a-mongo-db)))
     (is (function?(:my-put-item a-mongo-db)))
     (is (function?(:my-put-items a-mongo-db)))
(is (= (count a-mongo-db) 6))
))

)










(deftest unit-next-date-time-a
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2000-01")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2000-02")))))

(deftest unit-next-date-time-b
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2001-02-03")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2001-02-04")))))

(deftest unit-next-date-time-c
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2002-12")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2002-13")))))

(deftest unit-next-date-time-d
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2003-12-01")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2003-12-02")))))

(deftest unit-next-date-time-e
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2000")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2001")))))

(deftest unit-next-date-time-f
  (testing "count-string : cccccccccccccccccccccc "
    (let [future-date (next-date-time "2004-04-04-04")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2004-04-04-05")))))



(defn do-tests []
 (mongo-db-specs)
  (run-tests 'tests-mongo-db))


