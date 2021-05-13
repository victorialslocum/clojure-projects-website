(defn concat-L-A [L A]
  (if (empty? A)
    '()
    (cons (concat L (first A)) (concat-L-A L (rest A)))))

(concat-L-A (list 'a) (list (list 'c 'c) (list 'b 'd)))
