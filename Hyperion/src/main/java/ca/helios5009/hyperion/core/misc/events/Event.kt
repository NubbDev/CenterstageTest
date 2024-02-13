package ca.helios5009.core.misc.events

open class Event(val event: String) {

	open suspend fun run() {}
}