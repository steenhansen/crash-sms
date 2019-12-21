
(ns start-local-heroku-db

  (:gen-class)
  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.cron-service :refer [cron-init]])
  (:require [crash-screech.web-server :refer [make-request-fn web-init]])
  (:require [crash-screech.years-months :refer [instant-time-fn]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-screech.singular-service :refer [kill-services]])
  (:require [crash-screech.sms-event :refer [build-sms-send build-web-scrape]]))

(comment "to start"
         (-local-heroku-main USE_MONGER_DB HEROKU_CONFIG))
(defn -local-heroku-main

  ([db-type config-file] (-local-heroku-main db-type config-file IGNORE-ENV-VARS))

  ([db-type config-file environment-utilize]
   (kill-services)
   (let [ the-check-pages (make-check-pages 0)
           [my-db-obj web-port cron-url sms-data] (build-db PRODUCTION-COLLECTION
                                                          the-check-pages
                                                          db-type
                                                          config-file
                                                          environment-utilize)
         int-port (Integer/parseInt web-port)
         testing-sms? true
         temporize-func (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? instant-time-fn)
         request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms?)]
     (web-init int-port request-handler))))
