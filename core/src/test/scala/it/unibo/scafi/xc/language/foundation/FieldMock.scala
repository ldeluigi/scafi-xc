package it.unibo.scafi.xc.language.foundation

trait FieldMock:
  this: AggregateFoundation =>
  def mockField[T](items: Iterable[T]): AggregateValue[T]
