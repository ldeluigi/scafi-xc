package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.collections.ValueTree

/**
 * An Import consists of a map of value trees, where the key is the device id and the value is the value tree
 * corresponding.
 *
 * @tparam DeviceId
 *   the type of the device id
 * @tparam Token
 *   the type of the tokens that compose a path to a value in the tree
 * @tparam Value
 *   the type of the values in the tree
 */
type Import[DeviceId, Token, Value] = Map[DeviceId, ValueTree[Token, Value]]
