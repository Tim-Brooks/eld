(ns trees.tree)

(defn- create-node [{:keys [condition leaf? children value] :as node-map}]
  )

(defn tree [nodes]
  (mapv create-node nodes))
