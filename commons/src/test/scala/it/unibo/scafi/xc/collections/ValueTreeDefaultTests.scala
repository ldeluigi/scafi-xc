package it.unibo.scafi.xc.collections

import it.unibo.scafi.xc.UnitTest

class ValueTreeDefaultTests extends UnitTest with ValueTreeFactoryTests:
  "ValueTree default factory" should behave like valueTreeFactory(ValueTree)
