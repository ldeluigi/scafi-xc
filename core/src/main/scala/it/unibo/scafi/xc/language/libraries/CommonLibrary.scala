package it.unibo.scafi.xc.language.libraries

object CommonLibrary:
  def mux[T](cond: Boolean)(th: T)(el: T): T = if cond then th else el
