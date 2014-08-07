(ns trees.tree-test
  (:use [clojure.test])
  (:require [trees.tree :as tree]))

(def ^:private tree-nodes
  [{:condition (fn [feature-map] (> 1.0 (get feature-map "feature" 0)) 1 2)
    :leaf? false
    :children [1 2]}
   {:value 2
    :leaf? true}
   {:value 3
    :leaf? true}])

(deftest tree-construction
  (testing "Testing construction of simple tree"
    (let [{:keys [nodes
                  condition-func
                  value-func
                  leaf-func
                  children-func]} (tree/tree tree-nodes)
          [node1 node2 node3] (vec nodes)]
      (is (= false (leaf-func node1)))
      (is (= (:condition (nth tree-nodes 0)) (condition-func node1)))
      (is (= (:children (nth tree-nodes 0)) (vec (children-func node1))))
      (is (= true (leaf-func node2)))
      (is (= (:value (nth tree-nodes 1)) (value-func node2)))
      (is (= true (leaf-func node3)))
      (is (= (:value (nth tree-nodes 2)) (value-func node3))))))
