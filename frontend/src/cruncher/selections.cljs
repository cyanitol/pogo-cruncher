(ns cruncher.selections
  "Enable mass selections of pokemon."
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]
            [cruncher.utils.views :as vlib]))

(defn mass-selections
  "Function for mass selections of pokemon depending on a predicate.

   Example: Select all Pokemon which are not marked as favorite. The function
   call would then look like this: (mass-selections \"data-favorite\" \"true\" = true)"
  ([data-prop-str comparator operator checked]
   (let [rows (gdom/getElementsByClass "poketable-row")]
     (doall (map (fn [row]
                   (let [id (.getAttribute row "data-id")
                         data-prop (.getAttribute row data-prop-str)
                         checkbox (gdom/getElement (str "poketable-checkbox-" id))]
                     (if (operator comparator data-prop)
                       (set! (.. checkbox -checked) checked)
                       (set! (.. checkbox -checked) (not checked))))) rows))))
  ([data-prop-str comparator] (mass-selections data-prop-str comparator = true)))

(defn select-all-but-favorite []
  (mass-selections "data-favorite" "false"))

(defn unselect-all []
  (mass-selections "data-id" 0 not= false))

(defn select-all []
  (mass-selections "data-id" 0 < true))


;;;; Views
(defui SelectionButtons
       Object
       (render [this]
               (dom/div nil
                        (dom/p #js {:className "lead"} "Selections")
                        (vlib/button-primary select-all "Select all")
                        (vlib/button-primary unselect-all "Unselect all")
                        (vlib/button-primary select-all-but-favorite "Select all but favorite"))))
(def controls (om/factory SelectionButtons))
