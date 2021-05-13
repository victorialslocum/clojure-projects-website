;;

;;
(def vocabulary '(call me ishmael))
(def theta1 (list (/ 1 2) (/ 1 4) (/ 1 4)))
(def theta2 (list (/ 1 4) (/ 1 2) (/ 1 4)))
(def thetas (list theta1 theta2))
(def theta-prior (list (/ 1 2) (/ 1 2)))
(def my-corpus '((call me)
                 (call ishmael)))

(defn list-foldr [f base lst]
  (if (empty? lst)
    base
    (f (first lst)
       (list-foldr f base (rest lst)))))

(defn normalize [params]
  (let [sum (apply + params)]
    (map (fn [x] (/ x sum)) params)))

(defn flip [weight]
  (if (< (rand 1) weight)
    true
    false))

(defn sample-categorical [outcomes params]
  (if (flip (first params))
    (first outcomes)
    (sample-categorical (rest outcomes)
                        (normalize (rest params)))))

(defn repeat1 [f n]
  (if (= n 0)
    '()
    (cons (f) (repeat1 f (- n 1)))))

(defn sample-BOW-sentence [len probabilities]
  (if (= len 0)
    '()
    (cons (sample-categorical vocabulary probabilities)
          (sample-BOW-sentence (- len 1) probabilities))))

;; Problem 7
(defn sample-BOW-corpus [theta sent-len corpus-len]
  (repeat1 (fn [] (sample-BOW-sentence sent-len theta)) corpus-len))

(println (sample-BOW-corpus theta2 2 2))
;; output: a sample corpus that matches the format of my-corpus, such as ((call ishmael) (call me))

;; Problem 8
(defn sample-theta-corpus [sent-len corpus-len theta-probs]
  (let [theta (sample-categorical thetas theta-probs)]
    (list theta (sample-BOW-corpus theta sent-len corpus-len))))

(def theta-corpus (sample-theta-corpus 2 2 theta-prior))
(println theta-corpus)
;; output: a theta-corpora pair from thetas and sample-BOW-corpus

;;
(defn get-theta [theta-corpus]
  (first theta-corpus))

(defn get-corpus [theta-corpus]
  (first (rest theta-corpus)))

(defn sample-thetas-corpora [sample-size sent-len corpus-len theta-probs]
  (repeat1 (fn [] (sample-theta-corpus sent-len corpus-len theta-probs)) sample-size))

;; Problem 9
(defn ref-corpora [sample-size sent-len corpus-len theta-probs]
  (let [org-corpora (sample-thetas-corpora sample-size sent-len corpus-len theta-probs)]
    (list-foldr
     (fn [theta-corpus rest-corpora]
       (cons (get-corpus theta-corpus)
             rest-corpora))
     '()
     org-corpora)))

(defn estimate-corpus-marginal [corpus sample-size sent-len corpus-len theta-probs]
  (let [ref-corpora (ref-corpora sample-size sent-len corpus-len theta-probs)
        value-list (list-foldr (fn [input rest] (cons (if (= input corpus) 1 0) rest)) '() ref-corpora)]
    (/ (apply + value-list) sample-size)))


;; Problem 10


(println (estimate-corpus-marginal my-corpus 50 2 2 theta-prior))
;; (println (estimate-corpus-marginal my-corpus 4000 2 2 theta-prior))
;; sample-size 50: 0, 1/25, 0, 1/50, 1/50
;; sample-size 4000: 11/800, 41/4000, 41/4000, 27/2000, 51/4000

;; The values for the marginal probability calculated in problem 2 are similar to the counts gained from 
;; estimating the marginal probability with a 4000 count sample size, maybe +/- 0.002. However, sampling 
;; with a 50 sample size does not tell you much about the marginal probability and varies a lot. 

;;
(defn get-count [obs observation-list count]
  (if (empty? observation-list)
    count
    (if (= obs (first observation-list))
      (get-count obs (rest observation-list) (+ 1 count))
      (get-count obs (rest observation-list) count))))

(defn get-counts [outcomes observation-list]
  (let [count-obs (fn [obs] (get-count obs observation-list 0))]
    (map count-obs outcomes)))

;; Problem 11   
(defn gothrough [pairs observed-corpus]
  (if (= observed-corpus (get-corpus pairs))
    pairs
    '()))

(defn rejection-sampler [theta observed-corpus sample-size sent-len corpus-len theta-probs]
  (let [n-theta-corpus-pairs
        (sample-thetas-corpora sample-size sent-len corpus-len theta-probs)
        cleaned-list
        (list-foldr
         (fn [pairs rest-pairs]
           (concat (gothrough pairs observed-corpus) rest-pairs))
         '()
         n-theta-corpus-pairs)]
    (if (= 0 (count cleaned-list))
      0
      (/ (get-count theta cleaned-list 0) (/ (count cleaned-list) 2)))))


;; Problem 12


(println (rejection-sampler theta1 my-corpus 100 2 2 theta-prior))
;; output: a lot of 0s and two 1/2s and one 1
(println (rejection-sampler theta1 my-corpus 1000 2 2 theta-prior))
;; output: 0.6 +/- 0.3
(println (rejection-sampler theta1 my-corpus 3000 2 2 theta-prior))
;; output: 0.65 +/- 0.15
(println (rejection-sampler theta1 my-corpus 4000 2 2 theta-prior))
;; output: 0.65 +/- 0.075

;; Calling 100 samples doesn't tell you much about the distribution of theta1, calling 1000 samples is still
;; very variable, and 3000 is still variable. I would say that a stable estimate would
;; occur around 3500-4000. 
;; The number of samples needed is fairly high because you are taking a small amount of samples from a large 
;; of them. In order to get an accurate distribution, you need to have a very large beginning sample size so 
;; when you take the smaller sample size you still have enough data to get proper results.  