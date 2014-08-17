(ns eld.tree
  (:require [eld.node :as node]))

(set! *warn-on-reflection* true)

(defprotocol Tree
  (to-zipper [this])
  (get-node [this node-id]))

(defn score-tree-with-path [{:keys [nodes root]} features]
  (loop [node-id root
         path (transient [])]
    (let [modified-path (conj! path node-id)
          node (get-node nodes node-id)]
      (if (node/leaf? node)
        {:value (node/value node) :path (persistent! path)}
        (recur (nth (node/children node) ((node/condition node) features))
               modified-path)))))

(defn score-tree [{:keys [nodes root]} features]
  (loop [node (get-node nodes root)]
    (if (node/leaf? node)
      (node/value node)
      (recur (get-node nodes (nth (node/children node)
                                  ((node/condition node) features)))))))