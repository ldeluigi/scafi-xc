package it.unibo.scafi.xc.language.foundation

trait DeviceAwareAggregateFoundation:
  this: AggregateFoundation =>

  /**
   * The type of device identifiers.
   */
  type DeviceID

  /**
   * Device identifiers must be equatable.
   */
  given idEquality: CanEqual[DeviceID, DeviceID] = CanEqual.derived

  def self: DeviceID

  def neighbors: AggregateValue[DeviceID]

end DeviceAwareAggregateFoundation
