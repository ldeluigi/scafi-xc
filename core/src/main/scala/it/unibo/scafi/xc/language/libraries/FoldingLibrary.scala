package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

object FoldingLibrary:

  extension [T](using language: AggregateFoundation)(av: language.AggregateValue[T])
    def fold[A](base: A)(f: (A, T) => A): A = av.withSelf.foldLeft(base)(f)
    def nfold[A](base: A)(f: (A, T) => A): A = av.withoutSelf.foldLeft(base)(f)
