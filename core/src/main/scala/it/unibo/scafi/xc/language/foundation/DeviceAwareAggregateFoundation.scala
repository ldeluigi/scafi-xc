package it.unibo.scafi.xc.language.foundation

trait DeviceAwareAggregateFoundation:
  this: AggregateFoundation =>

  /**
   * The type of device identifiers.
   */
  type DeviceId

  /**
   * Device identifiers must be equatable.
   */
  given idEquality: CanEqual[DeviceId, DeviceId] = CanEqual.derived

  def self: DeviceId

  def device: AggregateValue[DeviceId]

end DeviceAwareAggregateFoundation
