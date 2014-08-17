(ns eld.implementation.reduce
  (:require [eld.node :as node]
            [eld.tree :as tree]))

(set! *warn-on-reflection* true)

(defn add-node [node new-tree parent-stack]
  (if (empty? parent-stack)
    (conj new-tree node)
    (let [^objects parent-pair (peek parent-stack)
          new-children (aget parent-pair 1)]
      (if (== (aget parent-pair 0) (count new-children))
        (aset ))
      )))

;; Hardcoded root right now
;; Slow stacks
;; new tree is vector
(defn reduce-tree [tree features new-tree-features]
  (let [new-tree []]
    (loop [node (tree/get-node tree 0)
           nodes-to-search []
           parent-stack []]
      (cond (node/leaf? node)
            (if (empty? nodes-to-search)
              (conj new-tree node)
              (conj new-tree node))                                          ;; Add Node

            (contains? new-tree-features (node/feature node))
            nil                                             ;; Add search direction

            :else
            (recur (tree/get-node (node/next-node-id node features))
                   features
                   new-tree-features
                   new-tree
                   nodes-to-search))
      (recur node nodes-to-search parent-stack))))