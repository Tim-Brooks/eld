(ns eld.benchmark
  (:use [clojure.test])
  (:require [criterium.core :as criterium]
            [eld.core :as eld]
            [eld.tree :as tree]
            [eld.reduce :as reduce]))

(def ^:private tree-nodes
  [{:condition (fn [feature-map] (if (> 1.0 (get feature-map "feature" 0)) 0 1))
    :feature   "feature"
    :branch?   true
    :children  [1 2]
    :id        0}
   {:value   2
    :branch? false
    :id      1}
   {:condition (fn [feature-map] (if (< 1.0 (get feature-map "feature2" 2)) 0 1))
    :feature   "feature2"
    :branch?   true
    :children  [3 4]
    :id        2}
   {:value   4
    :branch? false
    :id      3}
   {:value   5
    :branch? false
    :id      4}])

(deftest ^:benchmark score-tree-benchmark
  (testing "Score persistent tree"
    (let [tree (eld/eld-tree tree-nodes)]
      (criterium/with-progress-reporting
        (criterium/bench
          (tree/score-tree tree {"feature" 2 "feature2" 2}))))))

(deftest ^:benchmark reduce-benchmark
  (testing "Reduce persistent tree"
    (let [tree (eld/eld-tree tree-nodes)]
      (criterium/with-progress-reporting
        (criterium/bench
          (reduce/reduce-tree tree {"feature" 2 "feature2" 2} #{"feature"}))))))