(ns guis.crud
  (:require [replicant.alias :refer [defalias]]
            [clojure.string :as str]))

;; Challenges:
;; - Separating the domain and presentation logic
;; - Managing mutation
;; - Building a non-trivial layout
;;
;; The task is to build a frame containing:
;; - a listbox *L*
;;   - A view of the list of names in the database
;;   - Only one item can be selected at a time
;; - a textfield *prefix*
;;   - Typing in it immediately filters *L* on family name
;; - a pair of textfields *given-name* and *family-name*
;; - button *BC*
;;   - Clicking it adds the *given-name* and *family-name* to *L*
;; - button *BU*
;;   - Disabled unless an item in *L* is selected
;;   - Clicking it updates the selected item in *L* with
;;     *given-name* and *family-name*
;; - button *BD*
;;   - Disabled unless an item in *L* is selected
;;   - Clicking it removes the selected item in *L*
;;
;;
;; [input *prefix*]
;;
;; Emil, Hans        | [input *given-name*]
;; Mustermann, Max   |
;; Tisch, Roman      | [input *family-name*]
;;                   |
;; -----------------------------------------
;; *BC* *BU* *BD*

(defalias input [attrs [label]]
  [:fieldset.fieldset
   [:label.fieldset-legend label]
   [:input.input attrs]])

(defn get-people [state]
  (let [fnf (some-> state ::family-name-filter str/trim not-empty str/lower-case)]
    (cond->> (::people state)
      fnf (filter #(str/starts-with? (str/lower-case (:person/family-name %)) fnf))
      :then (sort-by (juxt :person/family-name :person/given-name)))))

(defn render-ui [state]
  [:div.grid.gap-4
   [:h1.text-lg "CRUD"]
   [input {:on {:input [[::set-family-name-filter [:event.target/value]]]}} "Filter"]
   [:div.grid.grid-cols-2.gap-4
    [:div.bg-base-200.rounded-md.shadow-md
     [:ul.list.max-h-72.overflow-scroll
      (for [person (get-people state)]
        [:li.list-row.cursor-pointer
         (str (:person/family-name person) ", " (:person/given-name person))])]]
    [:form.flex.flex-col.gap-4
     [input "Given name"]
     [input "Family name"]]]
   [:div.flex.gap-2
    [:button.btn.btn-primary "Create"]
    [:button.btn.btn-primary "Update"]
    [:button.btn.btn-primary "Delete"]]])

(def view
  {:id :crud
   :text "CRUD"
   :render #'render-ui
   :actions
   {::set-family-name-filter
    (fn [_ filter]
      [[:effect/assoc-in [::family-name-filter] filter]])}})
