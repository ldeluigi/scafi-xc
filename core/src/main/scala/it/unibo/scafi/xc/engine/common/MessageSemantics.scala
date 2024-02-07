package it.unibo.scafi.xc.engine.common

trait MessageSemantics:
  type Envelope

  protected def open[T](a: Envelope): T

  protected def close[T](a: T): Envelope
