(ns eld.implementation.array-test
  (:use [clojure.test])
  (:require [eld.core :as eld]
            [eld.node :as node]
            [eld.util.compare :as compare]
            [eld.tree :as tree]))

(def ^:private tree-nodes
  [{:condition (fn [feature-map] (if (> 1.0 (get feature-map "feature" 0)) 0 1))
    :feature   "feature"
    :branch?   true
    :children  [1 2]
    :id        "1"}
   {:value   2
    :branch? false
    :id      "2"}
   {:condition (fn [feature-map] (if (< 1.0 (get feature-map "feature2" 2)) 0 1))
    :feature   "feature2"
    :branch?   true
    :children  [3 4]
    :id        "3"}
   {:value   4
    :branch? false
    :id      "4"}
   {:value   5
    :branch? false
    :id      "5"}])

(deftest tree-construction
  (let [tree (eld/array-tree tree-nodes)]
    (doseq [[expected actual] (map vector tree-nodes (:nodes tree))]
      (testing (str "Comparing node " (:id expected) " with actual.")
        (is (compare/map-node-compare expected actual))))))

(deftest score-tree
  (let [tree (eld/array-tree tree-nodes)]
    (testing "Score tree returns expected value"
      (is (= 2 (tree/score-tree tree {"feature" 0 "feature2" 0})))
      (is (= 4 (tree/score-tree tree {"feature" 2 "feature2" 2})))
      (is (= 5 (tree/score-tree tree {"feature" 2 "feature2" 0})))
      (is (= 4 (tree/score-tree tree {"feature" 2})))
      (is (= 2 (tree/score-tree tree {}))))))

(deftest score-tree-with-path
  (let [tree (eld/array-tree tree-nodes)]
    (testing "Score tree with path returns expected value and path"
      (is (= {:value 2, :path [0 1]}
             (tree/score-tree-with-path tree {"feature" 0 "feature2" 0})))
      (is (= {:value 4, :path [0 2 3]}
             (tree/score-tree-with-path tree {"feature" 2 "feature2" 3})))
      (is (= {:value 5, :path [0 2 4]}
             (tree/score-tree-with-path tree {"feature" 2 "feature2" 0.5}))))))
