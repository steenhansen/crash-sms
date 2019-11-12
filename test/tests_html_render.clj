

(ns tests-html-render

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.scrape-html :refer [read-html]])
  (:require [crash-screech.html-render :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all])

  (:require [lambdaisland.deep-diff :as ddiff])

 (:require [text-diff :refer :all])
 
  )



(alias 's 'clojure.spec.alpha)
(alias 'h-r 'crash-screech.html-render)


(s/fdef h-r/day-hour-min
  :args (s/cat :check-date  :year-mon-day-hour-min?/test-specs ) )



(s/fdef h-r/get-two-months
  :args (s/cat :my-db-obj  coll?
               :yyyy-mm   :year-month?/test-specs   ) )



(s/fdef h-r/get-index
  :args (s/alt :unary (s/cat :my-db-obj  coll?   )
               :binary (s/cat :my-db-obj  coll?
                      :yyyy-mm   :year-month?/test-specs   ) ))


(defn html-render-specs []
 (if RUN-SPEC-TESTS
    (do
  (println "Speccing html-render")
      (spec-test/instrument)

      (spec-test/instrument 'day-hour-min))))

(deftest unit-day-hour-min
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "unit-day-hour-min"  "html-render")
    (let [expected-day-hour-min "05-06:07"
          actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
      (is (= expected-day-hour-min actual-day-hour-min)))))



 ;; (deftest unit-get-two-months
 ;;   (testing "test-day-hour-min : cccccccccccccccccccccc "
 ;;     (console-test  "unit-day-hour-min"  "html-render")
 ;;     (let [expected-day-hour-min "05-06:07"
 ;;         qqqq  actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
 ;;       (is (= expected-day-hour-min actual-day-hour-min)))))

(defn return-diffXX [char-1 char-2]
  (if (= char-1 char-2)
      ["." "-"]
       [char-1 char-2])
)

(defn str-diffXX [str-1 str-2]
 ( let [seq-1 (seq str-1)
        seq-2 (seq str-2)

      start-diff (map ())
   ]

  


 )

)

(deftest unit-get-index
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "unit-get-index"  "html-render")
    (let [db-type "monger-db"
          [my-db-obj _ _ _] (build-db DB-TABLE-NAME
                                      []
                                      db-type
                                      TEST-CONFIG-FILE
                                      IGNORE-ENV-VARS)
          purge-table (:purge-table my-db-obj)
          put-items (:put-items my-db-obj)
          NOV-2000-DATE "2000-11-11-11:11:11.011Z"
          DEC-2000-DATE "2000-12-12-12:12:12.012Z"
          NOV-2000-MONTH "2000-11"    ; 0 and 1 record
          DEC-2000-MONTH "2000-12"    ; 1 and 2 records
          test-many [{:the-url "november-1" :the-date NOV-2000-DATE :the-html "blah 1" :the-accurate true :the-time 1111}
                     {:the-url "december-2" :the-date DEC-2000-DATE :the-html "blah 22" :the-accurate true :the-time 2222}
                     {:the-url "december-2" :the-date DEC-2000-DATE :the-html "blah 333" :the-accurate true :the-time 3333}]
          read-from-disk false
          expected-get-index (rn-2-n (read-html "tests_html_render_1.html" read-from-disk))]
      (purge-table)
      (put-items test-many)
      (let [actual-get-index (rn-2-n (get-index my-db-obj "2000-12"))]

;       (ddiff/pretty-print (ddiff/diff expected-get-index actual-get-index) )
;(println "111111111111111111111111111111111111111111111111111111111" )
;(println "22" expected-get-index )
;(println "33" actual-get-index)
;(println "4444444444444444444444444444444444444444444444444444444444")

      (let [ [text-diff-1 text-diff-2] (is-html-eq expected-get-index actual-get-index)]
          (is (= text-diff-1 text-diff-2)))
; (is-html-eq expected-get-index actual-get-index )

 ;(is (= (is-html-eq expected-get-index actual-get-index) ""))

;(clojure.pprint/pprint (ddiff/diff expected-get-index actual-get-index))

    ;(is (= expected-get-index actual-get-index))
))))
;
; show-data

; show-data-cron




(defn do-tests []
 (html-render-specs)
  (run-tests 'tests-html-render))


