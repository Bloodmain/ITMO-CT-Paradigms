;(declare *parenthesis)
;(declare *bitxor)
;
;(def *all-chars (mapv char (range 0 128)))
;(def *digit (+char (apply str (filter #(Character/isDigit %) *all-chars))))
;(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
;(def *ws (+ignore (+star *space)))
;(def *int (+map read-string (+str (+plus *digit))))
;(def *number (+map read-string (+str (+seq (+opt (+char "-")) *int (+opt (+str (+seq (+char ".") *int)))))))
;(defn *string [name] (+str (apply +seq (mapv #(+char (str %)) name))))
;
;(def *available-variables (+str (+plus (+char "xyzXYZ"))))
;(def *unary-names (+or (+char "-") (*string "negate")))
;
;(def *constant (+map Constant *number))
;(def *variable (+map (comp Variable str) *available-variables))
;
;(defn *fold-left-assoc [op-s next-level]
;      (+seqf (fn [fr others] (reduce #((get-op (first %2)) %1 (last %2)) fr others))
;             (delay next-level)
;             (+star (+seq *ws op-s *ws (delay next-level)))))
;
;(def *unary (+or *constant (+seqf #((get-op %1) %2) *unary-names *ws (delay *parenthesis)) *variable))
;(def *parenthesis (+or (+seqn 1 (+char "(") *ws (delay *bitxor) *ws (+char ")")) *unary))
;(def *multiplicative (*fold-left-assoc (+char "*/") *parenthesis))
;(def *additive (*fold-left-assoc (+char "+-") *multiplicative))
;(def *bitand (*fold-left-assoc (+char "&") *additive))
;(def *bitor (*fold-left-assoc (+char "|") *bitand))
;(def *bitxor (*fold-left-assoc (+char "^") *bitor))
;
;(def parseObjectInfix (+parser (+seqn 0 *ws *bitxor *ws)))
