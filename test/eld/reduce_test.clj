(ns eld.reduce-test
  (:use [clojure.test])
  (:require [eld.core :as core]
            [eld.reduce :as reduce]
            [eld.util.compare :as compare]))

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

(comment
  (deftest test-reduce
    (let [tree (core/array-tree tree-nodes)]
      (testing "Single node returned if no features for new tree"
        (compare/map-tree-compare? tree-nodes (reduce/reduce-tree tree {} {}))))))
