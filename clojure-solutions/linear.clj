(defn same-vector-form?
  ([x] (or (vector? x) (number? x)))
  ([x y]
   (if (vector? x)
     (and (vector? x) (vector? y) (= (count x) (count y)) (every? identity (mapv same-vector-form? x y)))
     (and (number? x) (number? y))))
  ([x y & more]
   (and (same-vector-form? x y) (apply same-vector-form? y more))))

(defn v? [v] (and (vector? v) (every? number? v)))
(defn m? [m] (and (vector? m) (every? v? m)))
(defn t? [t] (or (number? t) (empty? t) (apply same-vector-form? t)))

(defn vfn [func]
  (fn f [& vectors]
    {:pre [(apply same-vector-form? vectors)]}
    (letfn
      [(applyFn [& vectors]
                (cond (number? (first vectors))          (apply func vectors)
                  (v? (first vectors))                   (apply mapv func vectors)
                  :else                                  (apply mapv applyFn vectors)))]
      (apply applyFn vectors))))

(defn applyScalars [func]
  (fn [v & lambdas]
    {:pre [(every? number? lambdas)]}
    (mapv #(func % (apply * lambdas)) v)))

(defn prefix? [a b]
  (= a (take (count a) b)))

(defn get-form [t]
  (cond (number? t) (list)
    (v? t)          (list (count t))
    :else           (conj (get-form (peek t)) (count t))))

(defn broadcast [form t]
  (cond (empty? form) t
    (number? t)       (into [] (repeat (peek form) (broadcast (rest form) t)))
    :else             (mapv (partial broadcast (rest form)) t)))

(defn tFn [func]
  (fn
    ([x]
     (func x))
    ([x & more]
     (let [all         (conj more x)
           forms       (mapv get-form all)
           min-form    (apply max-key count forms)
           broadcasted (mapv (partial broadcast min-form) all)]
       (apply func broadcasted)))))

; ---------------------------------------------

(def v+ (vfn +))
(def v- (vfn -))
(def v* (vfn *))
(def vd (vfn /))

(def v*s (applyScalars *))

(defn scalar [& vectors]
  (apply + (apply v* vectors)))

(defn vect
  ([x]
   (identity x))

  ([x y]
   {:pre [(same-vector-form? x y)]}
   (letfn [(xi*yj [i j] (* (get x i) (get y j)))]
     (vector (- (xi*yj 1 2) (xi*yj 2 1))
             (- (xi*yj 2 0) (xi*yj 0 2))
             (- (xi*yj 0 1) (xi*yj 1 0)))))

  ([x y & vectors] (reduce vect (conj vectors y x))))

(def m+ (vfn +))
(def m- (vfn -))
(def m* (vfn *))
(def md (vfn /))

(def m*s (applyScalars v*s))

(defn transpose [m]
  {:pre [(m? m)]}
  (apply mapv vector m))

(defn m*v [m, v]
  (mapv (fn [el] (scalar el v)) m))

(defn m*m
  ([m]
   (identity m))
  ([m1 m2]
   (transpose (mapv (fn [el] (m*v m1 el)) (transpose m2))))
  ([m1 m2 & matrixes] (reduce m*m (conj matrixes m2 m1))))

(def tb+ (tFn v+))
(def tb- (tFn v-))
(def tb* (tFn v*))
(def tbd (tFn vd))
