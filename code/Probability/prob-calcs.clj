;; This calculates the joint, marginal, conditional, and posterior predictions probabilities of a corpus. 

;;
(def vocabulary '(call me ishmael))
(def theta1 (list (/ 1 2) (/ 1 4) (/ 1 4)))
(def theta2 (list (/ 1 4) (/ 1 2) (/ 1 4)))
(def thetas (list theta1 theta2))
(def theta-prior (list (/ 1 2) (/ 1 2)))
(def my-corpus '((call me)
                 (call ishmael)))

(defn score-categorical [outcome outcomes params]
  (if (empty? params)
    (/ 1 0)
    (if (= outcome (first outcomes))
      (first params)
      (score-categorical outcome (rest outcomes) (rest params)))))

(defn list-foldr [f base lst]
  (if (empty? lst)
    base
    (f (first lst)
       (list-foldr f base (rest lst)))))

(defn log2 [n]
  (/ (Math/log n) (Math/log 2)))

(defn score-BOW-sentence [sen probabilities]
  (list-foldr
   (fn [word rest-score]
     (+ (log2 (score-categorical word vocabulary probabilities))
        rest-score))
   0
   sen))

(defn score-corpus [corpus probabilities]
  (list-foldr
   (fn [sen rst]
     (+ (score-BOW-sentence sen probabilities) rst))
   0
   corpus))

(defn logsumexp [log-vals]
  (let [mx (apply max log-vals)]
    (+ mx
       (log2
        (apply +
               (map (fn [z] (Math/pow 2 z))
                    (map (fn [x] (- x mx)) log-vals)))))))

(defn theta-corpus-joint [theta corpus theta-probs]
  (+ (score-corpus corpus theta) (log2 (first theta-probs))))

(println "Log of Joint Probability:")
(println (theta-corpus-joint theta1 my-corpus theta-prior))
(println "Joint Probability:")
(println (Math/pow 2 (theta-corpus-joint theta1 my-corpus theta-prior)))

(defn compute-marginal [corpus theta-probs]
  (logsumexp (list (theta-corpus-joint theta1 corpus theta-probs)
                   (theta-corpus-joint theta2 corpus theta-probs))))

(println "Log of Marginal Probability:")
(println (compute-marginal my-corpus theta-prior))
(println "Marginal Probability:")
(println (Math/pow 2 (compute-marginal my-corpus theta-prior)))

(defn compute-conditional-prob [theta corpus theta-probs]
  (- (theta-corpus-joint theta corpus theta-probs) (compute-marginal corpus theta-probs)))

(println "Log of Conditional Probability:")
(println (compute-conditional-prob theta1 my-corpus theta-prior))

(defn compute-conditional-dist [corpus theta-probs]
  (list-foldr
   (fn [theta rest-theta]
     (cons (compute-conditional-prob theta corpus theta-probs)
           rest-theta))
   '()
   thetas))

(println "Log of Conditional Distribution:")
(println (compute-conditional-dist my-corpus theta-prior))

(defn expo [lst]
  (list-foldr
   (fn [val rest-lst]
     (cons (Math/pow 2 val)
           rest-lst))
   '()
   lst))

(println "Conditional Distribution:")
(println (expo (compute-conditional-dist my-corpus theta-prior)))

;; The distribution of the values over theta is equal to one because the sum of probabilities 
;; should be equal to one. The function assigns a greater probability to theta1 because
;; my-corpus is a perfect fit to theta1, so based on the corpus it would assign theta1 a greater
;; probabiltiy than theta2.

(defn compute-posterior-predictive [observed-corpus new-corpus theta-probs]
  (let [conditional-dist (compute-conditional-dist observed-corpus theta-probs)]
    (compute-marginal new-corpus (expo conditional-dist))))

(println "Log of Posterior Prediction Probability")
(println (compute-posterior-predictive my-corpus my-corpus theta-prior))
(println "Posterior Prediction Probability")
(println (Math/pow 2 (compute-posterior-predictive my-corpus my-corpus theta-prior)))

;; This is the marginal probability of the corpus 'my-corpus' based on the new prior probabiltiy distribution,
;; (2/3, 1/3) vs the old one (1/2 1/2). This value is greater than the marginal probability found in problem
;; 2 because this value assigns higher probability to theta1, and my-corpus is a better representation of 
;; theta1 than theta2. 