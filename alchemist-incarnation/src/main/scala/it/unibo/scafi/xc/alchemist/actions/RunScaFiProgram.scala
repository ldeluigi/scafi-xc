package it.unibo.scafi.xc.alchemist.actions

import it.unibo.alchemist.model.actions.AbstractAction
import it.unibo.alchemist.model.{ Position as AlchemistPosition, * }
import it.unibo.scafi.xc.alchemist.device.ScaFiDevice
import it.unibo.scafi.xc.engine.Engine
import it.unibo.scafi.xc.engine.context.ContextFactory
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext

class RunScaFiProgram[Position <: AlchemistPosition[Position]](
    val node: Node[Any | Null],
    val time: TimeDistribution[Any | Null],
    val program: String,
) extends AbstractAction[Any | Null](node):
  private type DeviceId = Int
  private val programPath: Array[String] = program.split('.')
  private val classPath: String = programPath.take(programPath.length - 1).mkString("", ".", "$")
  private val clazz = Class.forName(classPath).nn
  private val module = clazz.getField("MODULE$").nn.get(clazz)
  private val method = clazz.getMethods.nn.toList.find(_.nn.getName.nn == programPath.last).get.nn

  @SuppressWarnings(Array("DisableSyntax.asInstanceOf"))
  private def runProgram(using context: BasicExchangeCalculusContext[Int]): Any =
    method.invoke(module, context).nn.asInstanceOf[Any]

  private object Factory extends ContextFactory[ScaFiDevice[Position], BasicExchangeCalculusContext[Int]]:

    override def create(network: ScaFiDevice[Position]): BasicExchangeCalculusContext[DeviceId] =
      BasicExchangeCalculusContext[Int](
        network.localId,
        network.receive(),
      )

  private val engine = Engine(
    net = node.asProperty(classOf[ScaFiDevice[Position]]),
    factory = Factory,
    program = runProgram,
  )

  override def execute(): Unit =
    val _ = engine.cycle()

  override def cloneAction(node: Node[Any | Null], reaction: Reaction[Any | Null]): Action[Any | Null] = ???

  override def getContext: Context = Context.NEIGHBORHOOD
end RunScaFiProgram
