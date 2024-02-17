package it.unibo.scafi.xc.engine

import scala.quoted.*

import it.unibo.scafi.xc.language.libraries.BranchingLibrary.branch
import it.unibo.scafi.xc.language.syntax.BranchingSyntax

object MacroEnhancedProgram:

  inline def enhance[T](inline e: T)(using inline lang: BranchingSyntax): T =
    ${ enhanceCode('{ e })(using '{ lang }) }

  private def enhanceCode[T](e: Expr[T])(using lang: Expr[BranchingSyntax])(using Type[T])(using
      Quotes,
  ): Expr[T] =
    new ExprMap:

      override def transform[TT](e: Expr[TT])(using Type[TT])(using Quotes): Expr[TT] =
        transformChildren(e) match
          case '{ if $c then $t else $e } => '{ branch(using $lang)($c)($t)($e) }
          case _ => e
    .transform(e)
