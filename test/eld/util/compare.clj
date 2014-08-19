(ns eld.util.compare
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(defn- leaf-compare [node-map node]
  (and (= false (node/branch? node))
       (= true (node/leaf? node))
       (= (:value node-map) (node/value node))))

(defn- branch-compare [node-map node]
  (and (= true (node/branch? node))
       (= false (node/leaf? node))
       (= (:feature node-map) (node/feature node))
       (= (:condition node-map) (node/condition node))
       (= (:children node-map) (vec (node/children node)))))

(defn map-node-compare [node-map node]
  (if (:branch? node-map)
    (branch-compare node-map node)
    (leaf-compare node-map node)))