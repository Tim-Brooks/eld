(ns eld.implementation.reduce
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(defn loop [node features new-tree nodes-to-search]
  (cond (node/leaf? node)
        (if (empty? nodes-to-search)
          new-tree
          nil)                                              ;; Add Node

        (contains? features (node/feature node))
        nil                                                 ;; Add search direction

        :else
        nil                                                 ;; Get next node and recur
        ))

(defn reduce-tree [tree features])