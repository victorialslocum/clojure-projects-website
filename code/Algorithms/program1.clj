(defn remove-last-element [l]
  (reverse (rest (reverse l))))

(remove-last-element '(1 2 3 4 5))