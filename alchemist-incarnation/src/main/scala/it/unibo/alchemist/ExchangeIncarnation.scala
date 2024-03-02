package it.unibo.alchemist

import it.unibo.alchemist.model.conditions.AbstractCondition
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.alchemist.model.nodes.GenericNode
import it.unibo.alchemist.model.reactions.Event
import it.unibo.alchemist.model.timedistributions.DiracComb
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.model.{ Position as AlchemistPosition, * }
import it.unibo.scafi.xc.alchemist.actions.RunScaFiProgram
import it.unibo.scafi.xc.alchemist.device.ScaFiDevice
import org.apache.commons.math3.random.RandomGenerator

class ExchangeIncarnation[Position <: AlchemistPosition[Position]] extends Incarnation[Any | Null, Position]:
  override def getProperty(node: Node[Any | Null], molecule: Molecule, property: String): Double = ???

  override def createMolecule(s: String): Molecule = SimpleMolecule(s)

  override def createConcentration(s: String): Any | Null = s // TODO: fix this // TODO: use cache

  @SuppressWarnings(Array("DisableSyntax.null"))
  override def createConcentration(): Any | Null = null

  override def createNode(
      randomGenerator: RandomGenerator,
      environment: Environment[Any | Null, Position],
      parameter: String,
  ): Node[Any | Null] =
    val node = GenericNode[Any | Null](environment)
    node.addProperty(ScaFiDevice[Position](node, environment, DoubleTime(1))) // TODO: take retention as parameter
    node

  override def createTimeDistribution(
      randomGenerator: RandomGenerator,
      environment: Environment[Any | Null, Position],
      node: Node[Any | Null],
      parameter: String,
  ): TimeDistribution[Any | Null] =
    val frequency = 1.0 // TODO: fix to parameter.toDoubleOption.getOrElse(1.0)
    val initialDelay = randomGenerator.nextDouble() / frequency
    DiracComb(DoubleTime(initialDelay), frequency)

  override def createReaction(
      randomGenerator: RandomGenerator,
      environment: Environment[Any | Null, Position],
      node: Node[Any | Null],
      timeDistribution: TimeDistribution[Any | Null],
      parameter: String,
  ): Reaction[Any | Null] =
    val event = Event[Any | Null](node, timeDistribution)
    event.setActions(
      java.util.List.of(createAction(randomGenerator, environment, node, timeDistribution, event, parameter)),
    )
    event

  override def createCondition(
      randomGenerator: RandomGenerator,
      environment: Environment[Any | Null, Position],
      node: Node[Any | Null],
      time: TimeDistribution[Any | Null],
      actionable: Actionable[Any | Null],
      additionalParameters: String,
  ): Condition[Any | Null] = new AbstractCondition[Any | Null](node):
    override def getContext: Context = Context.LOCAL
    override def getPropensityContribution: Double = 1
    override def isValid: Boolean = true

  override def createAction(
      randomGenerator: RandomGenerator,
      environment: Environment[Any | Null, Position],
      node: Node[Any | Null],
      time: TimeDistribution[Any | Null],
      actionable: Actionable[Any | Null],
      additionalParameters: String,
  ): Action[Any | Null] =
    RunScaFiProgram[Position](node, time, additionalParameters)
end ExchangeIncarnation
