
(ns tests-choose-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])
  (:require [crash-screech.choose-db :refer  :all])
  (:require [java-time :refer [local-date?]])
  (:require [crash-screech.config-args :refer [make-config]])
  (:require [prepare-tests :refer :all]))

(defn choose-db-specs []
  (if RUN-SPEC-TESTS
    (do
      (spec-test/instrument)
      (spec-test/instrument 'get-db-conn)
      (spec-test/instrument 'build-today-error?)
      (spec-test/instrument 'get-phone-nums)
      (spec-test/instrument 'build-db))))

(deftest uni-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (console-test "uni-get-phone-nums" "choose-db")
    (let [expected-phone-nums ["12345678901" "01234567890"]
          actual-phone-nums (get-phone-nums "12345678901,01234567890")]
      (is (= expected-phone-nums actual-phone-nums)))))

(defn uni-get-db-conn-type [db-type]
  (let [the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
        my-db-funcs (get-db-conn T-DB-TEST-NAME [] db-type the-config)]
    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))))

(deftest uni-get-db-conn-mongoDb
  (testing "test-build-db :"
    (console-test "uni-get-db-conn-mongoDb" "choose-db")
    (uni-get-db-conn-type "monger-db")))

(deftest uni-get-db-conn-dynoDb
  (testing "test-build-db :"
    (console-test "uni-get-db-conn-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (uni-get-db-conn-type    "amazonica-db"))))

(defn  uni-build-empty-month-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db T-DB-TEST-NAME
                                                  THE-CHECK-PAGES ; ["www.sffaudio.com"]
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url "www.sffaudio.com",
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate true,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is (true?   ((:empty-month? my-db-obj) "2000-01")))
    ((:put-item my-db-obj) test-one)
    (is (false?   ((:empty-month? my-db-obj) "2000-01")))))

(deftest uni-build-empty-month-mongoDb
  (testing "test-build-db :"
    (console-test "int-build-empty-month-mongoDb" "choose-db")
    (uni-build-empty-month-db  "monger-db")))

(deftest uni-build-empty-month-dynoDb
  (testing "test-build-db :"
    (console-test "int-build-empty-month-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (uni-build-empty-month-db     "amazonica-db"))))

(defn  build-today-error-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db T-DB-TEST-NAME
                                                  THE-CHECK-PAGES ; ["www.sffaudio.com"][]
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url "www.sffaudio.com",
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate false,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is (false?   ((:today-error? my-db-obj) "2000-01-01")))
    ((:put-item my-db-obj) test-one)
    (is (true?   ((:today-error? my-db-obj) "2000-01-01")))))

(deftest uni-build-today-error-mongoDb
  (testing "test-build-db :"
 (console-test "uni-build-today-error-mongoDb" "choose-db")
    (build-today-error-db  "monger-db")))

(deftest uni-build-today-error-dynoDb
  (testing "test-build-db :"
 (console-test "uni-build-today-error-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (build-today-error-db     "amazonica-db"))))

(defn uni-build-db-type [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db T-DB-TEST-NAME
                                                         []
                                                         db-type
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)]
    (is (function? (:delete-table my-db-obj)))
    (is (function? (:purge-table my-db-obj)))
    (is (function? (:get-all my-db-obj)))
    (is (function? (:get-url my-db-obj)))
    (is (function? (:put-item my-db-obj)))
    (is (function? (:put-items my-db-obj)))
    (is (is-string-number web-port))
    (is-url-dir cron-url)))

(deftest uni-build-db-mongoDb
  (testing "test-build-db :"
    (console-test "uni-build-db-mongoDb" "choose-db")
    (uni-build-db-type "monger-db")))

(deftest uni-build-db-dynoDb
  (testing "test-build-db :"
    (console-test "uni-build-db-mongoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (uni-build-db-type     "amazonica-db"))))

(deftest uni-build-db
  (testing "test-build-db :"
    (uni-build-db-mongoDb)
    (uni-build-db-dynoDb)))

