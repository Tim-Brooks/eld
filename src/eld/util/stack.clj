(ns eld.util.stack)

(definterface IntStack
   (^int pop [])
   (^int peek [])
   (push [^int x]))

(deftype Stack [^ints array ^int ^:unsynchronized-mutable counter]
  IntStack
  (^int pop [this])
  (^int peek [this])
  (push [this ^int x]))

(defn stack [inital-capactiy]
  (Stack. (int-array inital-capactiy) 0))