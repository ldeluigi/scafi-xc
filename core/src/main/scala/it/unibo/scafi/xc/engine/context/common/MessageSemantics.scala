package it.unibo.scafi.xc.engine.context.common

trait MessageSemantics:
  type Envelope

  protected def open[T](a: Envelope): T

  protected def close[T](a: T): Envelope

object MessageSemantics:

  trait Basic extends MessageSemantics:
    override type Envelope = Any

    override protected def open[T](a: Any): T = a match
      case t: T @unchecked => t
      case _ => throw new ClassCastException(s"Cannot cast $a to requested type")

    override protected def close[T](a: T): Any = a
