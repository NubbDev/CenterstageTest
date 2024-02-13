package org.firstinspires.ftc.teamcode.autonomous

import android.os.Build
import androidx.annotation.RequiresApi
import ca.helios5009.hyperion.core.CommandExecute
import ca.helios5009.hyperion.core.misc.events.EventListener
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "Blue Right", group = "Autonomous")
class BlueRight: LinearOpMode() {
	val listener = EventListener()
	@RequiresApi(34)
	override fun runOpMode() {
		val pathExecutor = CommandExecute("blueLeft.csv", listener)
		pathExecutor.execute()
	}

}