package ca.helios5009.hyperion.core.misc

import org.firstinspires.ftc.robotcore.internal.system.AppUtil

import kotlin.io.path.Path


class FileReader {
	fun getFile(path: String): String {
		println(AppUtil.ROOT_FOLDER.absolutePath)
		return AppUtil.FIRST_FOLDER.absolutePath + "/Hyperion/$path"
	}
}