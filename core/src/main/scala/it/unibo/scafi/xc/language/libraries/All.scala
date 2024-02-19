package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.implementations.CommonBoundaries

/**
 * This object is a container for all the standard libraries available. It is useful to import all the libraries with a
 * single import.
 */
object All:

  export BranchingLibrary.{ _, given }
  export CommonLibrary.{ _, given }
  export ExchangeCalculusLibrary.{ _, given }
  export FieldCalculusLibrary.{ _, given }
  export FoldingLibrary.{ _, given }
  export GradientLibrary.{ _, given }
  export MathLibrary.{ _, given }
  export CommonBoundaries.{ _, given }
