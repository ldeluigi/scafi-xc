package it.unibo.scafi.xc.language.syntax.abbreviated

trait FC[L] {

  extension (l: L) {
    def nbr[A](e: => A): A
    def rep[A](init: => A)(fun: A => A): A
  }
}
