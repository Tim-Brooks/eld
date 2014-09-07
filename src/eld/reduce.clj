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
          child-idx (aget parent-data 1)
          parent-idx (aget parent-data 0)]
      (aset new-children-array child-idx (count new-tree))
      (when (== child-idx (dec (alength new-children-array)))
        ;; Not mutated
        (node/set-children (nth new-tree parent-idx) new-children-array))
      (conj! new-tree node))))

(defn- add-to-search [new-parent-idx ^ints children ^Stack nodes-to-search ^Stack parent-stack]
  (let [num-of-children (alength children)
        new-children (int-array num-of-children)]
    (loop [i (dec num-of-children)]
      (when (not (== -1 i))
        (.push nodes-to-search (aget children i))
        (.push parent-stack (object-array [new-parent-idx i new-children]))
        (recur (dec i))))))

(defn reduce-tree [tree features new-tree-features]
  (loop [node-id (tree/root tree)
         new-tree (transient [])
         nodes-to-search (Stack.)
         parent-stack (Stack.)]
    (let [node (tree/get-node tree node-id)]
      (cond (node/leaf? node)
            (if (empty? nodes-to-search)
              (tree/new-tree tree
                             (persistent! (add-node node new-tree parent-stack)))
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
                     (add-node node new-tree parent-stack)
                     nodes-to-search
                     parent-stack))


            :else
            (recur (node/next-node-id node features)
                   new-tree
                   nodes-to-search
                   parent-stack)))))
