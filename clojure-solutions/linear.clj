(defn same-vector-form?
  ([x] (or (vector? x) (number? x)))
  ([x y]
   (cond
     (vector? x) (and
                  (vector? x)
                  (vector? y)
                  (= (count x) (count y))
                  (every? #(identity %) (mapv same-vector-form? x y)))
     :else       (and (number? x) (number? y))))
  ([x y & more]
   (and (same-vector-form? x y) (apply same-vector-form? (conj more y)))))

(defn v? [v] (and (vector? v) (every? number? v)))
(defn m? [m] (and (vector? m) (every? v? m)))
(defn t? [t] (or (number? t) (empty? t) (apply same-vector-form? t)))
(defn rect-m? [m] (and (m? m) (t? m)))

(defn vfn [func]
  (fn applyFn [& vectors]
    {:pre [(apply same-vector-form? vectors)]}
    (cond (number? (first vectors))          (apply func vectors)
      (v? (first vectors))                   (apply mapv func vectors)
      :else                                  (apply mapv applyFn vectors))))

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

(defn compatible?
  ([x] (t? x))
  ([x y] (apply prefix? (sort-by count [(get-form x) (get-form y)])))
  ([x y & more] (and (compatible? x y) (apply compatible? (conj more y)))))

(defn broadcast [t form]
  (cond (empty? form) (identity t)
    (number? t)       (into [] (repeat (peek form) (broadcast t (rest form))))
    :else             (mapv #(broadcast % (rest form)) t)))


(defn broadcastAndApply [func t1 t2]
  (let [form1 (get-form t1)
        form2 (get-form t2)]
    (cond (< (count form1) (count form2)) (func (broadcast t1 form2) t2)
      :else                               (func t1 (broadcast t2 form1)))))

(defn tFn [func]
  (fn
    ([x]
     (func x))
    ([x & more]
     (reduce (partial broadcastAndApply func) (conj more x)))))

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
  {:pre [(rect-m? m)]}
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
