(ns trees.tree-test
  (:use [clojure.test])
  (:require [trees.node :as node]
            [trees.tree :as tree]))

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

(defn- leaf-tests [expected actual]
  (is (= true (node/leaf? actual)))
  (is (= (:value expected) (node/value actual))))

(defn- branch-tests [expected actual]
  (is (= false (node/leaf? actual)))
  (is (= (:condition expected) (node/condition actual)))
  (is (= (:children expected) (vec (node/children actual)))))

(deftest tree-construction
  (let [tree (tree/tree tree-nodes)]
    (doseq [[expected actual] (map vector tree-nodes (:nodes tree))]
      (testing (str "Comparing node " (:id expected) " with actual.")
        (if (:leaf? expected)
          (leaf-tests expected actual)
          (branch-tests expected actual))))))
