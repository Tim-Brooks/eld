(ns eld.util.equality
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(defn- leaves-equal? [node-map node]
  (and (= false (node/branch? node))
       (= true (node/leaf? node))
       (= (:value node-map) (node/value node))))

(defn- branches-equal? [node-map node]
  (and (= true (node/branch? node))
       (= false (node/leaf? node))
       (= (:feature node-map) (node/feature node))
       (= (:condition node-map) (node/condition node))
       (= (:children node-map) (vec (node/children node)))))

(defn map-node-equal? [node-map node]
  (if (:branch? node-map)
    (branches-equal? node-map node)
    (leaves-equal? node-map node)))