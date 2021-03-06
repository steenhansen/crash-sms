(ns crash-sms.config-args
  (:require [edn-config.core :as edn-read])
  (:require [me.raynes.fs :as file-sys])
  (:require [global-consts-vars :refer :all]))

(defmacro compact-hash [& vars] (list `zipmap (mapv keyword vars) (vec vars)))

(defn read-config-file
  [config-file]
  (let [config-path (str (file-sys/absolute config-file))]
    (if (nil? (resolve 'SYSTEM-UNDER-TEST))
      (print-line "Trying to load" config-path))
    (try (edn-read/load-file config-file)
         (catch Exception e (print-line "Failed to load" (.getMessage e))))))

(defn merge-environment
  [accum-environment env-object]
  (let [env-key (key env-object)
        plain-key (name env-key)
        the-value (val env-object)
        system-env (System/getenv plain-key)
        system-environment (assoc-in accum-environment [env-key] system-env)
        program-environment (assoc-in accum-environment [env-key] the-value)]
    (if (System/getenv plain-key) system-environment program-environment)))

(defn make-config
  [db-type config-file environment-utilize]
  (let [config-vars (read-config-file config-file)
        db-keyword (keyword db-type)
        db-config (get config-vars db-keyword)
        heroku-config (:heroku-vars config-vars)
        program-config (merge heroku-config db-config)
        ignore-environmentals (= environment-utilize IGNORE-ENV-VARS)
        has-environmentals (reduce merge-environment {} program-config)]
    (if ignore-environmentals program-config has-environmentals)))
