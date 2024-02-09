package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.abstractions.BidirectionalFunction.<=>

class NetworkAdapter[DeviceId, TokenA, TokenB, ValueA, ValueB](
    val network: Network[DeviceId, TokenA, ValueA],
    val tokenAdapter: TokenA <=> TokenB,
    val valueAdapter: ValueA <=> ValueB,
) extends Network[DeviceId, TokenB, ValueB]:

  export network.localId

  override def send(e: Export[DeviceId, TokenB, ValueB]): Unit =
    network.send(
      e.map((path, messageTree) => path.map(tokenAdapter.backward) -> messageTree.map(valueAdapter.backward)),
    )

  override def receive(): Import[DeviceId, TokenB, ValueB] = network
    .receive()
    .map((id, valueTree) =>
      id -> valueTree.map((path, value) => path.map(tokenAdapter.forward) -> valueAdapter.forward(value)),
    )
end NetworkAdapter
