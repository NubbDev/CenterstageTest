package ca.helios5009.hyperion.core

import android.os.Build
import androidx.annotation.RequiresApi
import ca.helios5009.hyperion.core.misc.commands.*
import ca.helios5009.hyperion.core.misc.events.EventListener
import ca.helios5009.hyperion.core.misc.commands.Bezier
import ca.helios5009.hyperion.core.misc.commands.EventCall
import ca.helios5009.hyperion.core.misc.commands.Point
import ca.helios5009.hyperion.core.misc.commands.Wait
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap

@RequiresApi(Build.VERSION_CODES.R)
class CommandExecute(fileName: String, val eventListener: EventListener, val testOnly: Boolean = false) {
	var unParsedCommands = ""
	val gsonParse = Gson()


	init {
		val pathReader = AutonPaths()
		val commandsParse = CommandsParse()
		val pathSegment = pathReader.readPath(fileName)
		unParsedCommands = commandsParse.read(pathSegment)
	}

	fun execute() {
		val commands = gsonParse.fromJson(unParsedCommands, ArrayList<LinkedTreeMap<String, JsonObject>>()::class.java)

		for (command in commands) {
			command.keys.forEach { name  ->
				when (name.lowercase()) {
					"start" -> handleStart(gsonParse.fromJson(command[name].toString(), Point::class.java))

					"end" -> handleEnd(gsonParse.fromJson(command[name].toString(), EventCall::class.java))

					"line" -> handleLine(gsonParse.fromJson(command[name].toString(), Point::class.java))

					"spline" -> handleSpline(gsonParse.fromJson(command[name].toString(), ArrayList<LinkedTreeMap<String, JsonObject>>()::class.java))

					"wait" -> handleWait(gsonParse.fromJson(command[name].toString(), Wait::class.java))
				}
			}
		}
	}

	private fun handleStart(point: Point) {
		eventListener.call(point.event.message)
	}

	private fun handleEnd(event: EventCall) {
		eventListener.call(event.message)

	}

	private fun handleLine(point: Point) {
		eventListener.call(point.event.message)

	}

	/**
	 * Handles the wait command
	 * @param wait The wait command
	 */
	private fun handleWait(wait: Wait) {
		eventListener.call(wait.event.message)
		val waitType = gsonParse.fromJson(wait.wait_type.toString(), LinkedTreeMap<String, JsonObject>()::class.java)

		waitType.keys.forEach { waitName ->
			when (waitName.lowercase()) {
				"event" -> {
					if (testOnly) {
						val eventData = gsonParse.fromJson(waitType[waitName].toString(), EventCall::class.java)
						var event = ""
						while (event !== eventData.message) {
							if (eventListener.value.get().lowercase() ==  eventData.message.lowercase()) {
								event = eventData.message
							}
						}
					} else {
						Thread.sleep(2000)
					}
				}
				"time" -> {
					val eventData = gsonParse.fromJson(waitType[waitName].toString(), Int::class.java)
					Thread.sleep(eventData.toLong())
				}
			}
		}
	}

	private fun handleSpline(spline: ArrayList<LinkedTreeMap<String, JsonObject>>) {
		for (events in spline) {
			events.keys.forEach { event ->
				when (event.lowercase()) {
					"bezier" -> handleBezier(gsonParse.fromJson(events[event].toString(), Bezier::class.java))
					"wait" -> handleWait(gsonParse.fromJson(events[event].toString(), Wait::class.java))
				}
			}
		}
	}

	private fun handleBezier(bezier: Bezier) {

	}

}