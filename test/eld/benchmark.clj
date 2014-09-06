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
    :children  [1 4]
    :id        0}
   {:condition (fn [feature-map] (if (< 1.0 (get feature-map "feature3" 2)) 0 1))
    :feature   "feature3"
    :branch?   true
    :children  [2 3]
    :id        1}
   {:value   2
    :branch? false
    :id      2}
   {:value   3
    :branch? false
    :id      3}
   {:condition (fn [feature-map] (if (< 1.0 (get feature-map "feature2" 2)) 0 1))
    :feature   "feature2"
    :branch?   true
    :children  [5 8]
    :id        4}
   {:condition (fn [feature-map] (if (< 1.0 (get feature-map "feature4" 2)) 0 1))
    :feature   "feature4"
    :branch?   true
    :children  [6 7]
    :id        5}
   {:value   6
    :branch? false
    :id      6}
   {:value   7
    :branch? false
    :id      7}
   {:value   8
    :branch? false
    :id      8}])

(deftest ^:benchmark score-tree-benchmark
  (testing "Score persistent tree"
    (let [tree (eld/eld-tree tree-nodes)]
      (criterium/with-progress-reporting
        (criterium/bench
          (tree/score-tree tree {"feature" 2
                                 "feature2" 2
                                 "feature3" 2
                                 "feature4" 2}))))))

;; TODO This demonstrates a bug in reduce
(deftest ^:benchmark reduce-benchmark
  (testing "Reduce persistent tree"
    (let [tree (eld/eld-tree tree-nodes)]
      (criterium/with-progress-reporting
        (criterium/bench
          (reduce/reduce-tree tree
                              {"feature" 0 "feature2" 2}
                              #{"feature2" "feature4"}))))))