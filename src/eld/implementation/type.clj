(ns eld.implementation.type
  (:require [clojure.zip :as zip]
            [eld.node :as node]
            [eld.tree :as tree])
  (:import (java.util Arrays)))

(set! *warn-on-reflection* true)

(deftype PersistentNode [leaf? feature value ^ints children condition]
  node/Node
  (branch? [this] (not leaf?))
  (leaf? [this] leaf?)
  node/LeafNode
  (value [this] value)
  node/BranchNode
  (children [this] children)
  (condition [this] condition)
  (feature [this] feature)
  (next-node-id [this features]
    (aget children (condition features)))
  (set-children [this new-children]
    (PersistentNode. leaf? feature value (int-array new-children) condition))
  Object
  (toString [_]
    (str "PersistentNode{leaf?=" leaf?
         ", feature=" feature
         ", value=" value
         ", children=" (Arrays/toString children)
         ", condition=" condition "}")))

(deftype PersistentTree [^int root ^objects nodes]
  tree/Tree
  (to-zipper [_]
    (zip/zipper node/branch?
                (fn [node] (map #(aget nodes %) (node/children node)))
                (fn [node children]
                  (node/set-children node children))
                (aget nodes 0)))
  (get-node [_ node-id] (aget nodes node-id)))

(defn create-node [condition feature branch? children value]
  (if branch?
    (PersistentNode. false feature value (int-array children) condition)
    (PersistentNode. true feature value (int-array 0) condition)))

(defn create-node-from-map [{:keys [condition feature branch? children value]}]
  (create-node condition feature branch? children value))

(defn create-tree [node-maps]
  (let [nodes (mapv create-node-from-map node-maps)]
    (PersistentTree. 0 (object-array nodes))))