
; /test/core_test.clj  
; (-do-tests)


(ns core-test
  (:gen-class)
  (:require [clojure.test :refer :all])

  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log])
  (:require [clj-http.client :as http-client])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [java-time.local :as j-time])
  (:require [clojure.string :as clj-str])

  (:use [sff-global-consts])
  (:use [sff-global-vars])

  (:use [test-prepare])
  (:use [crash-screech.check-data])
  (:use [crash-screech.choose-db])
  (:use [crash-screech.html-render])
  (:use [crash-screech.scrape-html])
  (:use [crash-screech.sms-event])
  (:use [crash-screech.years-months])
  (:use [crash-screech.config-args])
  (:use [crash-screech.singular-service])
  (:use [crash-screech.check-data]))

(def ^:const TEST-DIR "crash_screech_test/")

(load (str TEST-DIR "check-data/the-tests"))

(load (str TEST-DIR "choose-db/the-tests"))

(load (str TEST-DIR "cron-service/a-set-of-tests"))

(load (str TEST-DIR "html-render/the-tests"))
(load (str TEST-DIR "scrape-html/the-tests"))
(load (str TEST-DIR "years-months/the-tests"))

(io.aviso.repl/install-pretty-exceptions)

(spec-test/instrument)

(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test :monger-db))

(defn check-data-tests []
  (test-count-string)
  (test-derive-data)
  (test-ensure-has-date)
  (test-prepare-data)
  (test-sub-string)
  (test-trunc-string)
  (test-uniquely-id))

(defn choose-db-tests []
  (test-get-phone-nums)
  (test-build-db-3333)
  (test-build-db-3334))

(defn cron-service-tests []
  (test-build-cron-func)
  ;(test-build-db-3333)
  ;(test-build-db-3334)
  )

(defn html-render-tests []
  (test-count-scrapes)
  (test-day-hour-min)
  (test-get-index)
  (test-get-two-months)
  (test-make-request-fn1)
  (test-make-request-fn2)
  (test-show-data))

(defn scrape-html-tests []
  (test-1000)
  (test-1001)
  (test-1002)
  (test-1003)
  (test-2000)
  (test-2001))

(defn years-months-tests []
  (test-adjusted-date)
  (test-current-month)
  (test-current-yyyy-mm)
  (test-ensure-date)
  (test-month-name_0)
  (test-month-name_1)
  (test-month-name_-1)
  (test-prev-month)
  (test-prev-yyyy-mm)
  (test-yyyy-mm-to-ints))

(defn test-suite []
 ; (check-data-tests)
  ;(choose-db-tests)
  (cron-service-tests)
  ;(html-render-tests)
  ;(scrape-html-tests)
  ;(years-months-tests)
  )

(defn test-ns-hook [] (test-suite))

(defn -do-tests []
   ; (try           
   ;   (println "Nice stack trace print example, via io.aviso/pretty")
   ;   ("nice_stack_trace_print")
   ;    (catch Exception e (clojure.repl/pst e))   
   ;  )

  (reset! *we-be-testing* true)
  (kill-services)
  (check-testing)
  (run-tests 'core-test))

