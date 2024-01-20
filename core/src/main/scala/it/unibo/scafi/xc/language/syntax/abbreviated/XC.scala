package it.unibo.scafi.xc.language.syntax.abbreviated

trait XC[L] {

  extension (l: L) {
    def xc[T](init: T)(f: T => T): T // TODO: f can return two values
  }
}
