(defn to-many-args [init body] (letfn
                                 [(test
                                    ([x] (init x))
                                    ([x y] (body x y))
                                    ([x y & more] (and (test x y) (apply test (conj more y)))))]
                                 (fn [& args] (apply test args))))

(def same-vector-form?
  (to-many-args #(or (vector? %) (number? %))
                (fn [x y]
                    (cond
                      (vector? x) (and
                                    (vector? x)
                                    (vector? y)
                                    (= (count x) (count y))
                                    (every? #(identity %) (mapv same-vector-form? x y)))
                      :else (and (number? x) (number? y))))
                ))

(defn v? [v] (and (vector? v) (every? number? v)))
(defn m? [m] (and (vector? m) (every? v? m)))
(defn t? [t] (or (number? t) (empty? t) (apply same-vector-form? t)))
(defn rect-m? [m] (and (m? m) (t? m)))

(defn vfn [func] (fn applyFn [& vectors]
                     {:pre  [(apply same-vector-form? vectors)]
                      :post [(same-vector-form? % (first vectors))]}
                     (cond (number? (peek (into [] vectors))) (apply func vectors)
                           (v? (peek (into [] vectors))) (apply mapv func vectors)
                           :else (apply mapv applyFn vectors))))

(defn applyScalars [func]
      (fn [v & lambdas]
          {:pre  [(and (vector? v) (every? #(identity %) (mapv number? lambdas)))]
           :post [(same-vector-form? v %)]}
          (mapv #(func % (reduce * lambdas)) v)))

(defn prefix? [a b]
      (= a (subvec (into [] b) 0 (count a))))

(defn get-form [t]
      {:pre [(t? t)]}
      (cond (number? t) (list)
            (v? t) (list (count t))
            :else (conj (get-form (peek t)) (count t))))

(def compatible?
  (to-many-args t?
                (fn [x y]
                    {:pre [(and (t? x) (t? y))]}
                    (apply prefix? (sort-by count [(get-form x) (get-form y)])))
                ))

(defn broadcast [t form]
      {:pre [(and (t? t) (prefix? (get-form t) form))]
       :post [(= form (get-form %))]}
      (cond (empty? form) (identity t)
            (number? t) (into [] (repeat (peek form) (broadcast t (rest form))))
            :else (mapv #(broadcast % (rest form)) t)))


(defn broadcastAndApply [func t1 t2]
      {:pre  [(or (compatible? t1 t2) (compatible? t2 t1))]
       :post [(and (t? %) (or (= (get-form %) (get-form t1)) (= (get-form %) (get-form t2))))]}
      (let [form1 (get-form t1) form2 (get-form t2)]
           (cond (< (count form1) (count form2)) (func (broadcast t1 form2) t2)
                 :else (func t1 (broadcast t2 form1))))
      )

(defn tFn [func]
      (fn ([x]
           {:pre [(t? x)]}
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
      {:pre  [(and (every? v? vectors) (apply same-vector-form? vectors))]
       :post [(number? %)]}
      (reduce + (apply v* vectors)))

(defn vect
      ([x]
       {:pre  [(and (v? x) (= (count x) 3))]
        :post [(= x %)]}
       (identity x))

      ([x y]
       {:pre  [(and (and (v? x) (= (count x) 3)) (same-vector-form? x y))]
        :post [(same-vector-form? x %)]}
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
      {:pre  [(rect-m? m)]
       :post [(or (empty? %)
                  (and (rect-m? %) (= (count %) (count (peek m))) (= (count (peek %)) (count m))))]}
      (apply mapv vector m))

(defn m*v [m, v]
      {:pre  [(and (rect-m? m) (v? v) (= (count v) (count (peek m))))]
       :post [(= (count %) (count m))]}
      (mapv (fn [el] (scalar el v)) m))

(defn m*m
      ([m]
       (identity m))

      ([m1 m2]
       {:pre  [(and (rect-m? m1) (rect-m? m2) (= (count m2) (count (peek m1))))]
        :post [(and (rect-m? %) (= (count %) (count m1)) (= (count (peek %)) (count (peek m2))))]}
       
       (transpose (mapv (fn [el] (m*v m1 el)) (transpose m2))))
      ([m1 m2 & matrixes] (reduce m*m (conj matrixes m2 m1))))

(def tb+ (tFn v+))
(def tb- (tFn v-))
(def tb* (tFn v*))
(def tbd (tFn vd))

;(println (broadcast [[1 2]] '(1 2 3)))
;(println (tb+ [1] [2]))
