(ns main-sff-audio
  (:gen-class)
  (:require [amazonica.aws.dynamodbv2 :as aws-dyn])
  (:require [clojure.string :as clj-str])
  (:require [clojure.main :as clj-main])
  (:require [edn-config.core :as edn-read])
  (:require [overtone.at-at :as at-at])
  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [monger.core :as mong-core])
  (:require [monger.collection :as mong-coll])
  (:require [monger.operators :refer :all])
  (:require [clj-http.client :as http-client])
  (:require [me.raynes.fs :as file-sys])
  (:require [ring.adapter.jetty :as ring-jetty])
  (:require [ring.middleware.reload :as ring-reload])
  (:require [ring.util.response :as ring-response])
  (:require [java-time])




 (:require [cheshire.core :refer :all])

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log]))

(load "global-consts")

(load "./sff_audio/config-args")
(load "./sff_audio/years-months")
(load "./sff_audio/check-data")
(load "./sff_audio/dynamo-db")
(load "./sff_audio/mongo-db")
(load "./sff_audio/choose-db")
(load "./sff_audio/temporize-event")
(load "./sff_audio/singular-service")
(load "./sff_audio/cron-service")
(load "./sff_audio/html-render")
(load "./sff_audio/scrape-html")

(load "heroku-main")  ;  (-main "monger-db" "./local-config.edn" "use-environment")
(load "local-main")   ;  (-local-main "monger-db" "./local-config.edn" "use-environment")

(load "send-sms")     ;  (-sms-test "monger-db" "../heroku-config.edn" "use-environment") 
(load "run-tests")    ;  (-run-tests)
