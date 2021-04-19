(defn concat-L-A [L A]
  (if (empty? A)
    '()
    (cons (concat L (first A)) (concat-L-A L (rest A)))))
