(ns eld.util.compare
  (:use [clojure.test])
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(defn- leaf-tests [expected actual]
  (is (= false (node/branch? actual)))
  (is (= true (node/leaf? actual)))
  (is (= (:value expected) (node/value actual))))

(defn- branch-tests [expected actual]
  (is (= true (node/branch? actual)))
  (is (= false (node/leaf? actual)))
  (is (= (:feature expected) (node/feature actual)))
  (is (= (:condition expected) (node/condition actual)))
  (is (= (:children expected) (vec (node/children actual)))))

(defn map-node-compare [node-map node])

(defn map-tree-compare [map-tree tree]
  (doseq [[expected actual] (map vector map-tree (:nodes tree))]
    (testing (str "Comparing node " (:id expected) " with actual.")
      (if (:branch? expected)
        (branch-tests expected actual)
        (leaf-tests expected actual)))))