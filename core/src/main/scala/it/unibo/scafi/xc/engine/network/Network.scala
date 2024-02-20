package it.unibo.scafi.xc.engine.network

/**
 * A network is a communication channel between devices. It is used to send and receive messages in form of [[Export]].
 * Additionally, it provides ids for the local device and could implement network based sensors used to create a
 * [[it.unibo.scafi.xc.engine.context.Context Context]].
 * @tparam DeviceId
 *   the type of the device id
 * @tparam Token
 *   the type of the tokens that compose a path to a value in the tree
 * @tparam Value
 *   the type of the values in the tree
 */
trait Network[DeviceId, Token, Value]:

  /**
   * @return
   *   the id of the local device
   */
  def localId: DeviceId

  /**
   * Sends messages to neighbors in the network.
   * @param e
   *   the messages to send
   */
  def send(e: Export[DeviceId, Token, Value]): Unit

  /**
   * Captures the last versions of value trees received from neighbors in the network. In the aggregate semantics of
   * networks, they must discard stale value trees, according to some expiration policy.
   * @return
   *   the messages received from neighbors in the network
   */
  def receive(): Export[DeviceId, Token, Value]
end Network
