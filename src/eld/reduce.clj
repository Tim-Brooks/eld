(ns eld.reduce
  (:require [eld.node :as node]
            [eld.tree :as tree])
  (:import (java.util Stack)))

(set! *warn-on-reflection* true)

(defn- add-node [node new-tree ^Stack parent-stack]
  (let [^objects parent-pair (.pop parent-stack)
        ^objects new-children (aget parent-pair 2)
        child-idx (aget parent-pair 1)
        ^int idx (aget parent-pair 0)]
    (aset new-children idx (count new-tree))
    (if (== child-idx (alength new-children))
      (do (node/set-children! (nth new-tree idx) new-children)
          (conj! new-tree node))
      (conj! new-tree node))))

(defn- add-to-search [parent-idx ^objects children ^Stack nodes-to-search ^Stack parent-stack]
  (loop [i 0 [child & rest] children]
    (.push nodes-to-search child)
    (.push parent-stack (object-array [parent-idx i (object-array (alength children))]))
    (recur (inc i) rest)))

;; new tree is vector
(defn reduce-tree [{:keys [nodes root]} features new-tree-features]
  (loop [node-id root
         new-tree (transient [])
         nodes-to-search (Stack.)
         parent-stack (Stack.)]
    (let [node (tree/get-node nodes node-id)]
      (cond (node/leaf? node)
            (if (empty? nodes-to-search)
              (persistent! (conj! new-tree node))
              (let [next (.pop nodes-to-search)]
                (recur next                                 ;; Assumes real node
                       (add-node node new-tree parent-stack)
                       nodes-to-search
                       parent-stack)))

            (contains? new-tree-features (node/feature node))
            nil

            :else
            (recur (node/next-node-id node features)
                   new-tree
                   nodes-to-search
                   parent-stack)))))