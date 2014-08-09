(ns eld.tree-test
  (:use [clojure.test])
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(def ^:private tree-nodes
  [{:condition (fn [feature-map] (if (> 1.0 (get feature-map "feature" 0)) 1 2))
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

(deftest score-tree
  (let [tree (tree/tree tree-nodes)]
    (testing "Score tree returns expected value"
      (is (= 2 (tree/score-tree tree {"feature" 0})))
      (is (= 3 (tree/score-tree tree {"feature" 2})))
      (is (= 2 (tree/score-tree tree {}))))))
