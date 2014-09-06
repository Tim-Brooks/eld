(ns eld.reduce
  (:require [eld.node :as node]
            [eld.tree :as tree])
  (:import (java.util Stack)))

(set! *warn-on-reflection* true)

(defn- add-node [node new-tree ^Stack parent-stack]
  (if (empty? parent-stack)
    (conj! new-tree node)
    (let [^objects parent-data (.pop parent-stack)
          ^ints new-children-array (aget parent-data 2)
          child-idx (aget new-children-array 1)
          parent-idx (aget parent-data 0)]
      (aset new-children-array child-idx (count new-tree))
      (when (== child-idx (alength new-children-array))
        (node/set-children (nth new-tree parent-idx) new-children-array))
      (conj! new-tree node))))

(defn- add-to-search [new-parent-idx ^ints children ^Stack nodes-to-search ^Stack parent-stack]
  (let [new-children (int-array (alength children))]
    (loop [i 0 [child & rest] children]
      (when (not (nil? child))
        (.push nodes-to-search child)
        (.push parent-stack (object-array [new-parent-idx i new-children]))
        (recur (inc i) rest)))))

;; new tree is vector
(defn reduce-tree [tree features new-tree-features]
  (loop [node-id (tree/root tree)
         new-tree (transient [])
         nodes-to-search (Stack.)
         parent-stack (Stack.)]
    (let [node (tree/get-node tree node-id)]
      (cond (node/leaf? node)
            (if (empty? nodes-to-search)
              (persistent! (add-node node new-tree parent-stack)) ;; Link to parent
              (let [next (.pop nodes-to-search)]
                (recur next
                       (add-node node new-tree parent-stack)
                       nodes-to-search
                       parent-stack)))

            (contains? new-tree-features (node/feature node))
            (do
              (add-to-search (count new-tree)
                             (node/children node)
                             nodes-to-search
                             parent-stack)
              (recur (.pop nodes-to-search)
                     (conj! new-tree node)
                     nodes-to-search
                     parent-stack))


            :else
            (recur (node/next-node-id node features)
                   new-tree
                   nodes-to-search
                   parent-stack)))))
