; --------------------------------
; Functional

(defn divide'
  ([x] (/ 1.0 x))
  ([x & args] (/ x (double (apply * args)))))
(defn negate' [x] (- x))
(defn meansq' [& args] (divide' (apply + (mapv #(* % %) args)) (count args)))
(defn rms' [& args] (Math/sqrt (apply meansq' args)))
(defn sumexp' [& args] (apply + (mapv #(Math/exp %1) args)))
(defn lse' [& args] (Math/log (apply sumexp' args)))

(def int-to-bool #(> %1 0))
(defn bool-to-int [x] (if x 1 0))
(defn boolwise [op]
  (fn [& args] (bool-to-int (apply op (mapv int-to-bool args)))))

(def and' (boolwise #(and %1 %2)))
(def or' (boolwise #(or %1 %2)))
(def xor' (boolwise not=))
(def not' (boolwise not))
(def impl' (boolwise #(or (not %1) %2)))
(def iff' (boolwise =))

(defn operation [oper]
  (fn [& args]
    (fn [values] (apply oper (mapv #(% values) args)))))

(def constant constantly)

(defn variable [name] (fn [values] (get values name)))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation divide'))
(def negate (operation negate'))
(def meansq (operation meansq'))
(def rms (operation rms'))

(def available-operations
  {'-      subtract
   '+      add
   '*      multiply
   '/      divide
   'negate negate
   'meansq meansq
   'rms    rms})

(defn base-parser [const variab operations]
  (fn parser [expression]
    (cond (number? expression) (const expression)
      (symbol? expression)     (variab (name expression))
      (and (list? expression)
           (not-empty expression)) (apply
                                    (get operations (first expression))
                                    (mapv parser (rest expression))))))

(def parseFunction
  (comp (base-parser constant variable available-operations) read-string))

; --------------------------------
; Object

(declare ZERO)
(declare ONE)
(declare NEGATIVE_ONE)

(definterface Evaluable
  (^Number evaluate [values])
  (^String toString [])
  (^String toStringInfix [])
  (diff [by]))

(deftype Constant' [val]
  Evaluable
  (evaluate [this values] val)
  (toString [this] (str val))
  (toStringInfix [this] (str val))
  (diff [this by] ZERO))

(deftype Variable' [name]
  Evaluable
  (evaluate [this values] (get values (clojure.string/lower-case (first name))))
  (toString [this] name)
  (toStringInfix [this] name)
  (diff [this by] (if (= name by) ONE ZERO)))

(declare Add)
(declare Multiply)

(defn rem-nth [n args] (concat (take n args) (drop (inc n) args)))

(deftype Operation [op symb partDiff args]
  Evaluable
  (evaluate [this values] (apply op (mapv #(.evaluate % values) args)))
  (toString [this]
    (str "(" symb " " (clojure.string/join " " (mapv #(.toString %) args)) ")"))
  (toStringInfix [this]
    (if (= (count args) 2)
      (str "(" (.toStringInfix (first args)) " " symb " " (.toStringInfix (last args)) ")")
      (str symb "(" (.toStringInfix (first args)) ")")))
  (diff [this by]
    (apply Add
           (map-indexed #(Multiply (.diff %2 by) (apply partDiff %1 %2 (rem-nth %1 args))) args))))

(defn evaluate [obj values] (.evaluate obj values))
(defn toString [obj] (.toString obj))
(defn toStringInfix [obj] (.toStringInfix obj))
(defn diff [obj by] (.diff obj by))

(defn Constant [val] (Constant'. val))
(defn Variable [name] (Variable'. name))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def NEGATIVE_ONE (Constant -1))

(defn create-op [op symb partDiff & args] (Operation. op symb partDiff args))

(def Add (partial create-op + "+" (constantly ONE)))
(def Subtract
  (partial create-op - "-"
           (fn ([i x] NEGATIVE_ONE)
             ([i x & others] (if (zero? i) ONE NEGATIVE_ONE)))))
(def Negate (partial create-op negate' "negate" (constantly NEGATIVE_ONE)))
(def Multiply (partial create-op * "*" (fn [i x & others] (apply Multiply others))))
(def Divide
  (partial create-op divide' "/"
           (fn ([i x] (Negate (Divide (Multiply x x))))
             ([i x & others]
              (if (zero? i)
                (Divide (apply Multiply others))
                (Negate (apply Divide (concat others [x x]))))))))
(def Sumexp
  (partial create-op sumexp' "sumexp"
           (fn [i x & others] (Sumexp x))))
(def LSE
  (partial create-op lse' "lse"
           (fn [i x & others] (Divide (Sumexp x) (apply Sumexp x others)))))

(def And (partial create-op and' "&&" (constantly ONE)))
(def Or (partial create-op or' "||" (constantly ONE)))
(def Xor (partial create-op xor' "^^" (constantly ONE)))
(def Not (partial create-op not' "!" (constantly ONE)))
(def Impl (partial create-op impl' "->" (constantly ONE)))
(def Iff (partial create-op iff' "<->" (constantly ONE)))

(def available-operations-object
  {'-            Subtract
   '+            Add
   '*            Multiply
   '/            Divide
   'negate       Negate
   'sumexp       Sumexp
   'lse          LSE
   '&&           And
   '||           Or
   (symbol "^^") Xor
   '!            Not
   '->           Impl
   '<->          Iff})

(def parseObject
  (comp (base-parser Constant Variable available-operations-object) read-string))

; --------------------------------
; Parser

(load-file "parser.clj")

(defn get-op [c] (get available-operations-object (symbol c)))
(defn reduce-op [perm]
  (letfn
    [(fold ([fr] fr)
           ([op1 op op2 & other] (apply fold (apply (get-op op) (perm [op1 op2])) other)))]
    (fn [& ops] (apply fold (perm ops)))))

(def reduce-left-assoc (reduce-op identity))
(def reduce-right-assoc (reduce-op reverse))

(defparser parseObjectInfix
  *all-chars (mapv char (range 0 128))
  *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
  *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars)))
  *ws (+ignore (+star *space))
  *int (+map read-string (+str (+plus *digit)))
  *number (+map read-string (+str (+seq (+opt (+char "-")) *int (+opt (+str (+seq (+char ".") *int))))))
  (*token [& names] (apply +or (mapv #(+str (apply +seq (mapv (fn [x] (+char (str x))) %))) names)))
  *available-variables (+str (+plus (+char "xyzXYZ")))
  *unary-names (*token "-" "negate" "!")
  *constant (+map Constant *number)
  *variable (+map (comp Variable str) *available-variables)
  (*operation [op-s reducer next-level]
              (+seqf (partial apply reducer) (delay next-level)
                     (+map flatten (+star (+seq *ws op-s *ws (delay next-level))))))
  *unary (+or *constant (+seqf #((get-op %1) %2) *unary-names *ws (delay *parenthesis)) *variable)
  *parenthesis (+or (+seqn 1 (+char "(") *ws (delay *iff) *ws (+char ")")) *unary)
  *multiplicative (*operation (*token "*" "/") reduce-left-assoc *parenthesis)
  *additive (*operation (*token "+" "-") reduce-left-assoc *multiplicative)
  *and (*operation (*token "&&") reduce-left-assoc *additive)
  *or (*operation (*token "||") reduce-left-assoc *and)
  *xor (*operation (*token "^^") reduce-left-assoc *or)
  *impl (*operation (*token "->") reduce-right-assoc *xor)
  *iff (*operation (*token "<->") reduce-left-assoc *impl)
  *expressionParser (+seqn 0 *ws *iff *ws))
