

(load "home-page") 
(load "check-data") 

(defn date-plus-1 [begins-with]
(let [  date-vector  (clj-str/split begins-with #"-") 
							 last-string (last date-vector)
        last-number (read-string last-string) 
							 last-plus1 (+ last-number 1)
        last-padded (left-pad last-plus1 2)
        but-last (pop date-vector)
        plus1-vector (conj but-last last-padded)
        date-plus1 (clj-str/join "-" plus1-vector)]
    date-plus1))

(defn mongolabs-build [mongolabs-config my-collection pages-to-check ]    
  (let [ mongodb-user-pass-uri (get mongolabs-config :MONGODB_URI) 
         {:keys [_conn db]} (mong-core/connect-via-uri mongodb-user-pass-uri)
         db-handle db]
				(defn put-items-monger [ check-records]
				  (let [ fixed-dates ( prepare-data check-records)]
						  (mong-coll/insert-batch db-handle my-collection fixed-dates)))

				(defn delete-table-monger []
		    (mong-coll/remove db-handle my-collection))
												
				(defn put-item-monger [check-record]
	     (let [ fixed-dates ( prepare-data [check-record] )
             fixed-rec (first fixed-dates)	]
         (mong-coll/insert db-handle my-collection fixed-rec)))

				(comment  "" ((:get-all-monger my-db-obj) "2019-05")         )
				(defn get-all-monger "" [begins-with]
		    (let [ date-plus1(date-plus-1 begins-with) ]
				    (println "mon" begins-with "---" date-plus1)
	       (mong-coll/find-maps db-handle my-collection {:_id { $gte begins-with $lt date-plus1 }}) ))

   	(comment  "year of www.sffaudio" ((:get-url-monger my-db-obj) "2019" "www.sffaudio.com")         )
				(defn get-url-monger "" [begins-with page-to-check]
				  (let [date-plus1 (date-plus-1 begins-with)]
				    (mong-coll/find-maps db-handle my-collection { $and [{:_id { $gte begins-with $lt date-plus1 }}
											                                                         {:check-url page-to-check}] }))))

    {:delete-table delete-table-monger
			  :get-all get-all-monger 
     :get-url get-url-monger 
     :put-item put-item-monger
     :put-items put-items-monger})


