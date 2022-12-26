(ns dialogue-demo
  (:require
   [reagent.core :as r]))

;; In a real application you'd want to have this either at the component level 
;; or, more likely, stored in a state DB
(def !current-step (r/atom :base))

(def dialogue 
  {:base {:description "First description"
          :options [{:display "Go Forward" :actions [{:type :key :value :second} {:type :quest :name "Fetch Quest"}]}
                    {:display "Third" :actions [{:type :key :value :third}]}]} 
   :second {:description "Second"
            :options [{:display "Leave" :actions [{:type :key :value :base}]}
                      {:display "Leave With Gold" :actions [{:type :key :value :base}{:type :gold :amount 20}]}]}
   :third {:description "Now You're On The Third option."
           :options [{:constraint true 
                      :false {:display "You Don't Have it"} 
                      :true {:display "You Do have It"}}]}})

; in a real application this would do something else and likely be its own multimethod flow
(defn check-constraint []
  true)

(defmulti handle-dialogue-action :type)

; In this example we'll reset the atom manually, but you'd probably want to setup 
; event/effect handlers for these operations
(defmethod handle-dialogue-action :key [action]
  (reset! !current-step (:value action)))

(defmethod handle-dialogue-action :quest [action]
  (js/alert (str "You started quest: " (:name action))))

(defmethod handle-dialogue-action :gold [action]
  (js/alert (str "You got: " (:amount action) " gold")))

(defn render-button [option]
  (let [display (:display option)]
    [:button {:on-click #(doseq [action (:actions option)] (handle-dialogue-action action)) :key display}
     display]))

(defn Dialogue-view [{:keys [description options]} dialogue-step]
  [:div
   [:p description]
   (for [option options]
     (if (:constraint option)
       (render-button ((-> (check-constraint) str keyword) option))
       (render-button option)))])

(defn main-panel [] 
  [Dialogue-view (@!current-step dialogue)])
