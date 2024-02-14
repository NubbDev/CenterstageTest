package ca.helios5009.hyperion.core.misc.events

abstract class Event(val event: String) {

	open suspend fun run() {}
}