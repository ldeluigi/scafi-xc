package it.unibo.scafi.xc.engine.stack

import it.unibo.scafi.xc.engine.path.Path

trait Stack[Token]:
  def current: Path[Token]

  def align(token: Token): Stack[Token]

  def dealign: Stack[Token]

object Stack:
  def apply[Token](): Stack[Token] = StackImpl()

  private case class StackImpl[Token](path: Path[Token] = List.empty) extends Stack[Token]:
    override def current: Path[Token] = path

    override def align(token: Token): Stack[Token] = StackImpl(token :: path)

    override def dealign: Stack[Token] = StackImpl(path.tail)
