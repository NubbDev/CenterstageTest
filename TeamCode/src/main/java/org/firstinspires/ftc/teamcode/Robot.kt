package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import kotlin.math.abs

class Robot(private val instance : LinearOpMode) {
	val fl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FL")
	val fr: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "FR")
	val br: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BR")
	val bl: DcMotorEx = instance.hardwareMap.get(DcMotorEx::class.java, "BL")

	val leftEncoder: DcMotorEx = fl
	val rightEncoder: DcMotorEx = fr
	val backEncoder: DcMotorEx = br
}