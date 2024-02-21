package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }

/**
 * An Export consists of a map of value trees, where the key is the device id and the value is the value tree
 * corresponding. The default value provides a tree to send to a device that is not present in the map.
 * @tparam DeviceId
 *   the type of the device id
 * @tparam Token
 *   the type of the tokens that compose a path to a value in the tree
 * @tparam Value
 *   the type of the values in the tree
 */
type Export[DeviceId, Token, Value] = MapWithDefault[DeviceId, ValueTree[Token, Value]]
