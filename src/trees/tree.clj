(ns trees.tree)

(set! *warn-on-reflection* true)

; not leaf: [leaf? condition children]
; leaf: [leaf? value]
(defn- create-node [{:keys [condition leaf? children value] :as node-map}]
  (if leaf?
    (to-array [true value])
    (to-array [false condition (to-array children)])))

(defn tree [node-maps]
  (let [nodes (mapv create-node node-maps)]
    {:nodes (to-array nodes)
     :size (count nodes)
     :condition-func (fn [^objects node] (aget node 1))
     :value-func (fn [^objects node] (aget node 1))
     :leaf-func (fn [^objects node] (aget node 0))
     :children-func (fn [^objects node] (aget node 2))}))
