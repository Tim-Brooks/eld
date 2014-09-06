(ns eld.tree
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(defprotocol Tree
  (to-zipper [this])
  (get-node [this node-id])
  (nodes [this])
  (root [this])
  (new-tree [this new-nodes]))

(defn score-tree-with-path [tree features]
  (loop [node-id (root tree)
         path (transient [])]
    (let [modified-path (conj! path node-id)
          node (get-node tree node-id)]
      (if (node/leaf? node)
        {:value (node/value node) :path (persistent! path)}
        (recur (node/next-node-id node features)
               modified-path)))))

(defn score-tree [tree features]
  (loop [node (get-node tree (root tree))]
    (if (node/leaf? node)
      (node/value node)
      (recur (get-node tree (node/next-node-id node features))))))