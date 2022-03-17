(def Person
  {:name "Person" :address {:street {:number 123 :name "Fun Street"}}})

(:number (:street (:address Person)))

(-> Person
    :address
    :street
    :number)

(def Home
  {:price 100 :new-furnace? false :energy-efficient? false})

(defn add-furnace [home]
  (conj home {:new-furnace? true}))

(defn make-energy-efficient [home]
  (conj home {:energy-efficient? true}))

(defn update-price [home]
  (update home :price + 50))

(update-price (make-energy-efficient (add-furnace Home)))

(-> Home
   add-furnace
   make-energy-efficient
   update-price)

(defn update-price-with-value [home value]
  (update home :price + value))

(-> Home
  add-furnace
  make-energy-efficient
  (update-price-with-value 200))


(def STOCK_PRICES [{:name "company1" :price 5 } {:name "company 2" :price 10}])

(->> STOCK_PRICES
    (map #(update % :price inc))
    (filter #(> (:price %) 10))
    (into []))

(into [] (filter #(> (:price %) 10)(map #(update % :price inc) STOCK_PRICES)))


(def Persons
  {:name "Person" :addresses [{:street {:number 123 :name "Fun Street"}} {:street {:number 456 :name "Fun Street"}}]})

(-> Persons
  :addresses
  ( #(filter (fn [address] (= (:number (:street address)) 123)) %)))


(def Guy
  {:name "Peter"})

(-> Guy
  :name)
