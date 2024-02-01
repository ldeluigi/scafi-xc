package it.unibo.scafi.xc.language.syntax

trait CommonSyntax {
  def mux[T](cond: Boolean)(th: T)(el: T): T = if (cond) th else el
}
