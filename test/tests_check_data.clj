
(ns tests-check-data
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.check-data :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [text-diff :refer [is-html-eq]])
  (:require [prepare-tests :refer :all]))

(alias 's 'clojure.spec.alpha)
(alias 'c-d 'crash-screech.check-data)

(s/fdef c-d/prepare-data
  :args (s/cat :check-records (s/coll-of :url-date-tuple?/test-specs)))

(s/fdef c-d/count-string
  :args (s/cat :hay-stack string?
               :needle-regex #(instance? java.util.regex.Pattern %)))

(s/fdef c-d/trunc-string
  :args (s/cat :the-string string? :num-chars integer?))

(s/fdef c-d/derive-data
  :args (s/cat :check-record :data-map?/test-specs))

(s/fdef c-d/uniquely-id
  :args (s/cat :may-index integer? :many-item :check-map?/test-specs))

(s/fdef c-d/ensure-has-date
  :args (s/cat :check-record :url-date-tuple?/test-specs))

(defn check-data-specs []
  (if RUN-SPEC-TESTS
    (do
  (println "Speccing check-data")
  (spec-test/instrument)
  (spec-test/instrument 'count-string)
  (spec-test/instrument 'trunc-string)
  (spec-test/instrument 'derive-data)
  (spec-test/instrument 'uniquely-id)
  (spec-test/instrument 'ensure-has-date)
  (spec-test/instrument 'prepare-data))))

(deftest unit-count-string
  (testing "count-string : qqqcccccccccccccccccccccc "
    (let [occurance-count (count-string "001001001000" #"1")]
      (console-test "unit-count-string" "check-data")
      (is (= occurance-count 3)))))

(deftest unit-trunc-string
  (testing "test-trunc-string : cccccccccccccccccccccc "
    (let [trunced-str (trunc-string "123456789" 3)]
      (console-test "unit-trunc-string" "check-data")
      (is (= trunced-str "123")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

(def ^:const T-BEFORE-THE-DATA {:the-url "www.sffaudio.com",
                                :the-date "2019-06-19-01:54:03.800Z",
                                :the-html "123456789",
                                :the-accurate true,
                                :the-time FAKE-TEST-TIME})
;                                :the-time 9876543})

(def ^:const T-AFTER-CHECK-DATA {:check-url "www.sffaudio.com",
                                 :check-date "2019-06-19-01-54-03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time FAKE-TEST-TIME})

(deftest unit-derive-data
  (testing "test-dervive-data : cccccccccccccccccccccc "
    (let [derived-data (derive-data T-BEFORE-THE-DATA)]
      (console-test "unit-derive-data" "check-data")
      (is (= derived-data T-AFTER-CHECK-DATA)))))

(def ^:const T-BEFORE-UNIQUE-ID {:check-url "www.sffaudio.com",
                                 :check-date "2019-06-19-01:54:03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time 1234})

(def ^:const T-EXPECTED-UNIQUE-ID {:check-url "www.sffaudio.com",
                                   :check-date "2019-06-19-01:54:03.800Z",
                                   :_id "2019-06-19-01:54:03.800Z+1",
                                   :check-html "123456789",
                                   :check-bytes 9
                                   :check-accurate true,
                                   :check-time 1234})

(deftest unit-uniquely-id
  (testing "test-uniquely-id : cccccccccccccccccccccc "
    (let [unique-data (uniquely-id 1 T-BEFORE-UNIQUE-ID)]
      (console-test  "unit-uniquely-id"  "check-data")
      (is (= unique-data T-EXPECTED-UNIQUE-ID)))))


;;;;;;;;;;;;;;


(def ^:const T-ENSURE-DATA {:the-url "www.sffaudio.com",
                            :the-html "123456789",
                            :the-accurate true,
                            :the-time 1234})

(deftest unit-ensure-has-date
  (testing "est-ensure-has-date : DDDDDDDDDDDDD "
    (let [has-date (ensure-has-date T-ENSURE-DATA)]
      (console-test "unit-ensure-has-date" "check-data")
      (is (contains? has-date :the-date)))))


;;;;;;;;;;;;;;;;;;;;


(def ^:const T-BEFORE-ENSURE-DATA

  [{:the-url "www.sffaudio.com",
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "blah 1111",
    :the-accurate true,
    :the-time 1234}
   {:the-url "sffaudio.herokuapp.com_rsd_rss",
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "bluh 2222",
    :the-accurate true,
    :the-time 12346}])

(def ^:const T-EXPECTED-PREPARE-DATA
  [{:check-url "www.sffaudio.com",
    :check-date "2019-06-19-01-54-03.800Z",
    :_id "2019-06-19-01-54-03.800Z+0",
    :check-html "blah 1111",
    :check-bytes 9
    :check-accurate true,
    :check-time 98765432}
   {:check-url "sffaudio.herokuapp.com_rsd_rss",
    :_id "2019-06-19-01-54-03.800Z+1",
    :check-date "2019-06-19-01-54-03.800Z",
    :check-html "bluh 2222",
    :check-bytes 9
    :check-accurate true,
    :check-time 98765432}])

(deftest unit-prepare-data
  (testing "prepare-tests-data : fffff "
    (let [prepared-data (prepare-data T-BEFORE-ENSURE-DATA)

               [diff-1 diff-2] (is-html-eq prepared-data T-EXPECTED-PREPARE-DATA)

         ]
      (console-test "unit-prepare-data" "check-data")
; (ddiff/pretty-print (ddiff/diff prepared-data T-EXPECTED-PREPARE-DATA) )
;(println "11" prepared-data )



   (is (= diff-1 diff-2))   ;; so here we are comparaing strings, so much easier  ;; abbacab

;(println "22" "----" )
;(println "33" T-EXPECTED-PREPARE-DATA)
;;;;;;;;;;;;
      (is (= prepared-data T-EXPECTED-PREPARE-DATA)))))

(defn do-tests []
  (check-data-specs)
  (run-tests 'tests-check-data))


