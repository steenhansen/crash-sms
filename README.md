﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿### Rational ﻿﻿﻿Crash-sms is a Clojure program that runs on Heroku at https://fathomless-woodland-85635.herokuapp.com/ which checks a list of websites every 2 hours and texts my phone when a website does not conform to a valid website. Non cached website pages are checked for database, WordPress, and Node.js crashes. The Heroku add-on Till Mobile handles the sms messages, while Temporize Scheduler calls the cron jobs.### How to run program in Emacs with Cider```    core> (-local-main USE_MONGER_DB LOCAL_CONFIG)```### How to view program```        http://localhost:8080/```### How to check web pages```        http://localhost:8080/url-for-cron-execution```