package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.Export

/**
 * A context implements all the logic to handle value tree manipulation and message exchange from a device to itself and
 * its neighbors. Ideally, a context is created from inbound messages and outputs outbound messages as a result of some
 * computation.
 * @tparam DeviceId
 *   the type of the device identifiers
 * @tparam Token
 *   the type used to track the location of values in value trees
 * @tparam Value
 *   the type that wraps all the values exchanged between devices
 */
trait Context[DeviceId, Token, Value]:

  /**
   * @return
   *   the [[Export]] that contains all the inbound value trees from self and neighbors.
   */
  def inboundMessages: Export[DeviceId, Token, Value]

  /**
   * @return
   *   the [[Export]] that contains all the outbound value trees to self and neighbors.
   */
  def outboundMessages: Export[DeviceId, Token, Value]
end Context
