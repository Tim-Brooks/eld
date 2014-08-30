(ns eld.implementation.type
  (:require [clojure.zip :as zip]
            [eld.node :as node]
            [eld.tree :as tree])
  (:import (java.util Arrays)))

(set! *warn-on-reflection* true)

;; Need to decide on rules for when a node is a leaf and when it is not.
(deftype PersistentNode [id leaf? feature value ^ints children condition]
  node/Node
  (id [this] id)
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
    (PersistentNode. id false feature value (int-array new-children) condition))
  Object
  (toString [_]
    (str "PersistentNode{id=" id
         ", leaf?=" leaf?
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
                  (node/set-children node
                                     (map (fn [node]
                                            (if (instance? PersistentNode node)
                                              (node/id node)
                                              node))
                                          children)))
                (aget nodes 0)))
  (get-node [_ node-id] (aget nodes node-id))
  Object
  (toString [_]
    (str "PersistentTree{root=" root ", nodes=" (Arrays/toString nodes))))

(defn create-node [id condition feature branch? children value]
  (if branch?
    (PersistentNode. id false feature value (int-array children) condition)
    (PersistentNode. id true feature value (int-array 0) condition)))

(defn create-node-from-map
  [{:keys [id condition feature branch? children value]}]
  (create-node id condition feature branch? children value))

(defn create-tree [node-maps]
  (let [nodes (mapv create-node-from-map node-maps)]
    (PersistentTree. 0 (object-array nodes))))