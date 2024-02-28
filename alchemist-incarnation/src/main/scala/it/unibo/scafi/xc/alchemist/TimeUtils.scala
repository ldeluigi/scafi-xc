package it.unibo.scafi.xc.alchemist

import it.unibo.alchemist.model.Time

object TimeUtils:
  given Ordering[Time] = _.compareTo(_)
