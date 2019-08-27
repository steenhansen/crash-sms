

(ns tests-dynamo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.config-args :refer [make-config compact-hash]])
  (:require [crash-screech.dynamo-db :refer :all])

  (:require [java-time :refer [local-date?]])


  (:require [prepare-tests :refer :all])
)

(defn dynamo-db-specs []
  (if RUN-SPEC-TESTS
    (do
      (spec-test/instrument)
      (spec-test/instrument 'dynamo-build)
)))


(deftest uni-dynamo-build
  (testing "count-string : cccccccccccccccccccccc "
    (let [ db-type  "amazonica-db"
           the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
          pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least 1}]
                   a-dynamo-db       (dynamo-build the-config  T-DB-TEST-NAME pages-to-check)]
      (console-test "uni-dynamo-build" "dynamo-db")

     (is (function?(:my-delete-table a-dynamo-db)))
     (is (function?(:my-purge-table a-dynamo-db)))
     (is (function?(:my-get-all a-dynamo-db)))
     (is (function?(:my-get-url a-dynamo-db)))
     (is (function?(:my-put-item a-dynamo-db)))
     (is (function?(:my-put-items a-dynamo-db)))
(is (= (count a-dynamo-db ) 6))
))

)