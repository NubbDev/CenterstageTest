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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.RobotLog

class CommandExecute(val opMode: LinearOpMode, fileName: String, val eventListener: EventListener, val testOnly: Boolean = false) {
	var unParsedCommands = ""
	val gsonParse = Gson()
	val commandsParse = CommandsParse()
	lateinit var motors: Motors
	lateinit var odometry: Odometry
	lateinit var movement: Movement



	init {
		val pathReader = AutonPaths()
		val pathSegment = pathReader.readPath(fileName)
		unParsedCommands = commandsParse.read(pathSegment)
	}

	fun execute() {
		val commands = gsonParse.fromJson(unParsedCommands, ArrayList<LinkedTreeMap<String, JsonObject>>()::class.java)
		RobotLog.vv("Hyperion", "Commands: $commands")

		for (command in commands) {
			if (opMode.opModeIsActive()) {
				command.keys.forEach { name  ->
					opMode.telemetry.addData("Command", name)
					opMode.telemetry.update()
					if (opMode.opModeIsActive()) {
						when (name.lowercase()) {
							"start" -> handleStart(gsonParse.fromJson(command[name].toString(), Point::class.java))

							"end" -> handleEnd(gsonParse.fromJson(command[name].toString(), EventCall::class.java))

							"line" -> handleLine(gsonParse.fromJson(command[name].toString(), Point::class.java))

							"spline" -> handleSpline(gsonParse.fromJson(command[name].toString(), ArrayList<LinkedTreeMap<String, JsonObject>>()::class.java))

							"wait" -> handleWait(gsonParse.fromJson(command[name].toString(), Wait::class.java))
						}
					}
					opMode.sleep(1000)
				}
			}
		}
	}

	private fun handleStart(point: Point) {
		if (!opMode.opModeIsActive()) {
			return
		}
		odometry.setOrigin(point.x, point.y, point.rot)
		eventListener.call(point.event.message)
	}

	private fun handleEnd(event: EventCall) {
		if (!opMode.opModeIsActive()) {
			return
		}
		eventListener.call(event.message)
	}

	private fun handleLine(point: Point) {
		if (!opMode.opModeIsActive()) {
			return
		}
		eventListener.call(point.event.message)
		movement.start(arrayListOf(point))
	}

	/**
	 * Handles the wait command
	 * @param wait The wait command
	 */
	private fun handleWait(wait: Wait) {
		if (!opMode.opModeIsActive()) {
			return
		}
		eventListener.call(wait.event.message)
		val waitType = gsonParse.fromJson(wait.wait_type.toString(), LinkedTreeMap<String, JsonObject>()::class.java)

		waitType.keys.forEach { waitName ->
			when (waitName.lowercase()) {
				"event" -> {
					if (!testOnly) {
						val eventData = gsonParse.fromJson(waitType[waitName].toString(), EventCall::class.java)
						var event = ""
						while (event !== eventData.message) {
							opMode.telemetry.addData("Waiting for", eventData.message.lowercase())
							opMode.telemetry.addData("Current", eventListener.value.get().lowercase())
							opMode.telemetry.addData("Statement", eventListener.value.get().lowercase() ==  eventData.message.lowercase())
							opMode.telemetry.update()
							if (eventListener.value.get().lowercase() ==  eventData.message.lowercase()) {
								event = eventData.message
							}
						}
					} else {
						opMode.sleep(2000)
					}
				}
				"time" -> {
					val eventData = gsonParse.fromJson(waitType[waitName].toString(), Int::class.java)
					opMode.sleep(eventData.toLong())
				}
			}
		}
	}

	private fun handleSpline(spline: ArrayList<LinkedTreeMap<String, JsonObject>>) {
		if (!opMode.opModeIsActive()) {
			return
		}
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
		if (!opMode.opModeIsActive()) {
			return
		}

		val points = commandsParse.bezier(bezier.start, bezier.control[0], bezier.control[1], bezier.end)
		movement.start(points)
	}

}