package ca.helios5009.hyperion.core

import ca.helios5009.hyperion.core.misc.commands.Point
import com.qualcomm.robotcore.util.RobotLog
import kotlin.math.pow

//@SuppressLint("UnsafeDynamicallyLoadedCode")
class CommandsParse {
	init {
		RobotLog.vv("Hyperion", "Loading Hyperion Library")
		System.loadLibrary("hyperion")
	}

	external fun read(commands: String): String

	fun bezier(pt0: Point, ct0: Point, ct1: Point, pt1: Point): MutableList<Point> {
		val points = mutableListOf<Point>()
		val resolution = 20

		for (i in 0..resolution) {
			val t = i.toDouble() / resolution

			val x = (1 - t).pow(3)  * pt0.x +
					3 * (1 - t).pow(2) * t * ct0.x +
					3 * (1 - t) * t.pow(2) * ct1.x +
					t.pow(3) * pt1.x

			val y = (1 - t).pow(3)  * pt0.y +
					3 * (1 - t).pow(2) * t * ct0.y +
					3 * (1 - t) * t.pow(2) * ct1.y +
					t.pow(3) * pt1.y

			when(t) {
				0.0 -> points.add(Point(x, y, 0.0, pt0.event))
				1.0 -> points.add(Point(x, y, 0.0, pt1.event))
				0.25 -> points.add(Point(x, y, 0.0, ct0.event))
				0.75 -> points.add(Point(x, y, 0.0, ct1.event))
				else -> points.add(Point(x, y, 0.0))
			}
		}

		return points
	}

}