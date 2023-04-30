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
   'rms    rms
   })

(defn base-parser [const variab operations]
      (fn parser [expression]
          (cond (number? expression) (const expression)
                (symbol? expression) (variab (name expression))
                (and (list? expression)
                     (not-empty expression)) (apply
                                               (get operations (first expression))
                                               (mapv parser (rest expression))))))

(def parseFunction (comp (base-parser constant variable available-operations) read-string))

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
         (evaluate [this values] (get values name))
         (toString [this] name)
         (toStringInfix [this] name)
         (diff [this by] (if (= name by) ONE ZERO)))

(declare Add)
(declare Multiply)

(defn rem-nth [n args] (concat (take n args) (drop (inc n) args)))
(deftype Operation [op symb partDiff args]
         Evaluable
         (evaluate [this values] (apply op (mapv #(.evaluate % values) args)))
         (toString [this] (str "(" symb " " (clojure.string/join " " (mapv #(.toString %) args)) ")"))
         (toStringInfix [this] (if (= (count args) 2)
                                 (str "(" (.toStringInfix (first args)) " " symb " " (.toStringInfix (last args)) ")")
                                 (str symb "(" (.toStringInfix (first args)) ")")))
         (diff [this by] (apply Add (map-indexed #(Multiply (.diff %2 by) (apply partDiff %1 %2 (rem-nth %1 args))) args))))

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
(def Subtract (partial create-op - "-" (fn ([i x] NEGATIVE_ONE)
                                           ([i x & others] (if (zero? i) ONE NEGATIVE_ONE)))))
(def Negate (partial create-op negate' "negate" (constantly NEGATIVE_ONE)))
(def Multiply (partial create-op * "*" (fn [i x & others] (apply Multiply others))))
(def Divide (partial create-op divide' "/"
                     (fn ([i x] (Negate (Divide (Multiply x x))))
                         ([i x & others] (if (zero? i)
                                           (Divide (apply Multiply others))
                                           (Negate (apply Divide (concat others [x x]))))))))
(def Sumexp (partial create-op sumexp' "sumexp"
                     (fn [i x & others] (Sumexp x))))
(def LSE (partial create-op lse' "lse"
                  (fn [i x & others] (Divide (Sumexp x) (apply Sumexp x others)))))

(def available-operations-object
  {'-      Subtract
   '+      Add
   '*      Multiply
   '/      Divide
   'negate Negate
   'sumexp Sumexp
   'lse    LSE
   })

(def parseObject (comp (base-parser Constant Variable available-operations-object) read-string))

; --------------------------------
; Parser

(load-file "parser.clj")
(defn -show [result]
      (if (-valid? result)
        (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
        "!"))
(defn tabulate [parser inputs]
      (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs))


(declare *parenthesis)
(declare *additive)
(defn get-op [c] (get available-operations-object (symbol (str c))))

(def *all-chars (mapv char (range 0 128)))
(def *digit (+char (apply str (filter #(Character/isDigit %) *all-chars))))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(def *int (+map read-string (+str (+plus *digit))))
(def *number (+map read-string (+str (+seq (+opt (+char "-")) *int (+opt (+str (+seq (+char ".") *int)))))))
(defn *string [name] (+str (apply +seq (mapv #(+char (str %)) name))))

(def *available-variables (+char "xyz"))
(def *unary-names (+or (+char "-") (*string "negate")))

(def *constant (+map Constant *number))
(def *variable (+map (comp Variable str) *available-variables))

(defn *fold-left-assoc [op-s next-level]
      (+seqf (fn [fr others] (reduce #((get-op (first %2)) %1 (last %2)) fr others))
             (delay (next-level))
             (+star (+seq *ws op-s *ws (delay (next-level))))))

(defn *unary [] (+or *constant (+seqf #((get-op %1) %2) *unary-names *ws (delay (*parenthesis))) *variable))
(defn *parenthesis [] (+or (+seqn 1 (+char "(") *ws (delay (*additive)) *ws (+char ")"))
                           (*unary)))
(defn *multiplicative [] (*fold-left-assoc (+char "*/") *parenthesis))
(defn *additive [] (*fold-left-assoc (+char "+-") *multiplicative))

(def parseObjectInfix (+parser (+seqn 0 *ws (*additive) *ws)))
