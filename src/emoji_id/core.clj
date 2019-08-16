(ns emoji-id.core
  (:gen-class))

(set! *warn-on-reflection* true)

(def dict [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R \S \T \U \V \W \X \Y \Z \a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z])

(defn code-point [^String emoji]
  (.codePointAt emoji (.offsetByCodePoints emoji 0 0)))

(def start-point (code-point "🐀"))

(defn emojify [c]
  (let [code-point (+ (int c) start-point)]
    (String/valueOf (char-array [(Character/highSurrogate code-point) (Character/lowSurrogate code-point)]))))

(defn ->int [v]
  (-> v str BigInteger.))

(defn encode [dict value]
  (let [base (-> dict count ->int)]
    (loop [result ()
           remaining (-> value ->int)
           exponent (int 1)]
      (if (.equals ^BigInteger remaining BigInteger/ZERO)
        (apply str (map emojify result))
        (let [a (.pow ^BigInteger base exponent)
              b (.mod ^BigInteger remaining ^BigInteger a)
              c (.pow ^BigInteger base (dec exponent))
              d (.divide b ^BigInteger c)]
          (recur (cons (dict (.intValue d)) result)
                 (.subtract ^BigInteger remaining b)
                 (inc exponent)))))))

(defn memoized-dict-map [dict]
  (into {}
        (map-indexed
         (fn [i c] [c (->int i)])
         dict)))

(defn demojify [s]
  (for [c (.. ^String s codePoints toArray)]
    (char (- c start-point))))

(defn decode [dict value]
  (let [chars (reverse (demojify value))
        base (-> dict count ->int)
        dict-map (memoized-dict-map dict)]
    (loop [bi BigInteger/ZERO
           exponent 0
           [c & remaining] chars]
      (if c
        (let [a (dict-map c)
              b (.multiply (.pow ^BigInteger base exponent) a)]
          (recur (.add bi (->int b))
                 (inc exponent)
                 remaining))
        bi))))

(defn -main [option value]
  (println
   (condp = option
     ":encode" (encode dict value)
     ":decode" (decode dict value)
     (str "unkown option" option "\nvalid options are: :encode or :decode"))))
