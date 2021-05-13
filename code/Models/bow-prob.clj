;; This computes the BOW probabilities of 

(load-file "uniform-bow-prob.clj")

(defn sample-BOW-sentence [len vocabulary probabilities]
  (if (= len 0)
    '()
    (cons (sample-categorical vocabulary probabilities)
          (sample-BOW-sentence (- len 1) vocabulary probabilities))))

;; Problem 7
(def moby-word-probabilities (normalize moby-word-frequencies))

(println (sample-BOW-sentence 3 moby-vocab moby-word-probabilities))

;; (every to involuntarily)
;; (then me little)
;; (and men knocking)
;; (but for it)
;; (me toward soul)

;; Problem 9
(defn lookup-probability [w outcomes probs]
  (if (empty? outcomes)
    0
    (if (= w (first outcomes))
      (first probs)
      (lookup-probability w (rest outcomes) (rest probs)))))

;; helper function
(defn product [l]
  (apply * l))

;; Problem 10
(defn compute-BOW-prob [sentence vocabulary probabilities]
  (if (empty? sentence)
    1
    (product (concat (list (lookup-probability (first sentence) vocabulary probabilities))
                     (list (compute-BOW-prob (rest sentence) vocabulary probabilities))))))

;; Problem 11
(compute-BOW-prob (list 'every 'to 'involuntarily) moby-vocab moby-word-probabilities)
;; output: 5/8998912
(compute-BOW-prob (list 'then 'me 'little) moby-vocab moby-word-probabilities)
;; output: 5/4499456
(compute-BOW-prob (list 'and 'men 'knocking) moby-vocab moby-word-probabilities)
;; output: 7/8998912
(compute-BOW-prob (list 'but 'for 'it) moby-vocab moby-word-probabilities)
;; output: 1/2249728
(compute-BOW-prob (list 'me 'toward 'soul) moby-vocab moby-word-probabilities)
;; output: 5/8998912

;; The probabilties defined above differ in value, while the probabilties in number 6 don't. This is because
;; the words of the sentence have different probabilites. The uniform BOW model doesn't rely on individual
;; probabilites of the words, while the standard BOW model does. Along with that, the probabilties in 11 are 
;; smaller than that in 6 because it becomes less likely to have a 3 word sentence with individual word 
;; probabilties.  


(compute-BOW-prob (list 'me 'toward 'soul) moby-vocab moby-word-probabilities)
;; output: 5/8998912
(compute-BOW-prob (list 'CALL 'me 'Ishmael) moby-vocab moby-word-probabilities)
;; output: 5/8998912
;; these have the same probabilty because two of the words are 1/208 frequency and the other is 5/208 frequency.
;; However, any permutations of these sentences where the words are in different orders will also have the same
;; probability. This is due to the bad of words model not relying on the order to determine the probability of
;; a sentence. Simply the individual probabilities of the words will suffice to determine the probability of a 
;; sentence. 