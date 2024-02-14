package ca.helios5009.hyperion.core.misc

import ca.helios5009.hyperion.core.misc.commands.Point
import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(p1: Point, p2: Point): Double {
	return sqrt((p2.x - p1.x).pow(2.0) + (p2.y - p1.y).pow(2.0))
}
