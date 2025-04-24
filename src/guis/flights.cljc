(ns guis.flights)

;; The task is to build a frame containing:
;;
;; - a combobox C with the two options "one-way flight" and "return flight"
;;   - initial value: "one-way flight"
;; - a textfield T1 representing the start date
;;   - initial value: arbitrary date
;; - a textfield T2 representing the return date
;;   - initial value: arbitrary date
;;   - initially disabled
;; - a button B for submitting the selected flight
;;
;; - T2 is enabled if, and only if, C’s value is "return flight".
;;
;; - When a non-disabled textfield T has an ill-formatted date then:
;;   - T is colored red
;;   - B is disabled
;;
;; - When:
;;   - C has the value "return flight"
;;   - T2’s date is strictly before T1’s
;;   => Then B is disabled.
;;
;; - When clicking B a message is displayed informing the user of their selection
;;   (e.g. "You have booked a one-way flight on 2025.04.24.").

(defn parse-date [s]
  (when (string? s)
    (when-let [[_ y m d] (re-find #"(\d\d\d\d).(\d\d).(\d\d)" s)]
      (str y "-" m "-" d))))

(defn format-inst [inst]
  (parse-date (pr-str inst)))

(defn prepare-departure-date [state]
  (cond-> {:value (or (::departure-date state)
                      (format-inst (:now state)))}
    (::departure-date state)
    (assoc :invalid? (-> (::departure-date state)
                         parse-date
                         nil?))))

(defn prepare-return-date [state flight-type departure-date]
  (let [enabled? (= flight-type :roundtrip)]
    (cond-> {:value (or (::return-date state)
                        (:value departure-date))
             :disabled? (not enabled?)}
      (and enabled? (::return-date state))
      (assoc :invalid? (-> (::return-date state)
                           parse-date
                           nil?)))))

(defn before? [a b]
  (< (compare a b) 0))

(defn get-form-state [state]
  (let [flight-type (or (::type state) :one-way)
        departure-date (prepare-departure-date state)
        return-date (prepare-return-date state flight-type departure-date)]
    {::type (or (::type state) :one-way)
     ::departure-date departure-date
     ::return-date return-date
     ::button {:disabled? (or (:invalid? departure-date)
                              (:invalid? return-date)
                              (and (= :roundtrip flight-type)
                                   (before? (:value return-date)
                                            (:value departure-date))))}}))
