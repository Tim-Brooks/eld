(ns trees.tree-test
  (:use [clojure.test])
  (:require [trees.tree :as tree]))

(def ^:private tree-nodes
  [{:condition (fn [feature-map] (> 1.0 (get feature-map "feature" 0)) 1 2)
    :leaf? false
    :children [1 2]
    :id "1"}
   {:value 2
    :leaf? true
    :id "2"}
   {:value 3
    :leaf? true
    :id "3"}])

(defn- leaf-tests [{:keys [leaf-func value-func]} expected actual]
  (is (= true (leaf-func actual)))
  (is (= (:value expected) (value-func actual))))

(defn- branch-tests [{:keys [leaf-func condition-func children-func]} expected actual]
  (is (= false (leaf-func actual)))
  (is (= (:condition expected) (condition-func actual)))
  (is (= (:children expected) (vec (children-func actual)))))

(deftest tree-construction
  (let [tree (tree/tree tree-nodes)]
    (doseq [[expected actual] (map vector tree-nodes (:nodes tree))]
      (testing (str "Comparing node " (:id expected) " with actual.")
        (if (:leaf? expected)
          (leaf-tests tree expected actual)
          (branch-tests tree expected actual))))))
