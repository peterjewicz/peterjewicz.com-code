; (:require [clojure.spec.alpha :as spec])
; Link to article: http://peterjewicz.com/using-clojure-spec-to-validate-api-routes/


(def POSITION_VALUES ["Developer" "Manager" "Marketing" "Design"])

(defn check-position [val]
  (some #(= val %) POSITION_VALUES))

(spec/def ::name string?)
(spec/def ::age int?)
(spec/def ::favorite-food string?)
(spec/def ::position check-position)

(spec/def ::person (spec/keys :req-un [::name ::age ::favorite-food ::position]))

(spec/valid? ::person {:name "Peter" :age "asd" :favorite-food "steak" :position "Developer"}) ; false
(spec/valid? ::person {:name "Peter" :age 27 :favorite-food "steak" :position "Developer"}) ; true

; example of explain-data
(spec/explain-data ::person {:name "Peter" :age "asd" :favorite-food "steak" :position "Developer"}) ; eror

(defn add [person]
  {:pre [(spec/valid? ::person person)]}
  (println "It Works!"))

(defn handle-api-error [data]
  (let [validation-error (spec/explain-data ::person data)
        problem-param (first (:path (first (second (first validation-error)))))]
    {:status 400 :body (str "Issue With " \'(name problem-param)\' " Value.")}))

(defn add-person [person]
  (try
     (do
       (add person)
       {:status 200 :body "Person Added"})
     (catch Error e (handle-api-error person))))