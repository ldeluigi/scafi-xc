package it.unibo.scafi.xc.engine

import scala.quoted.*

import it.unibo.scafi.xc.language.libraries.BranchingLibrary.branch
import it.unibo.scafi.xc.language.syntax.BranchingSyntax

object MacroEnhancedProgram:

  def enhance[T](e: Expr[T])(using lang: Expr[BranchingSyntax])(using Type[T])(using Quotes): Expr[T] = new ExprMap:

    override def transform[TT](e: Expr[TT])(using Type[TT])(using Quotes): Expr[TT] =
      transformChildren(e) match
        case '{ if $c then $t else $e } => '{ branch(using $lang)($c)($t)($e) }
        case _ => e
  .transform(e)
