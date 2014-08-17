(ns eld.implementation.reduce
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(set! *warn-on-reflection* true)

(defn loop [node features new-tree-features new-tree nodes-to-search]
  (cond (node/leaf? node)
        (if (empty? nodes-to-search)
          new-tree
          nil)                                              ;; Add Node

        (contains? new-tree-features (node/feature node))
        nil                                                 ;; Add search direction

        :else
        (recur (tree/get-node (node/next-node-id node features))
               features
               new-tree-features
               new-tree
               nodes-to-search)))

(defn reduce-tree [tree features new-tree-features])