package ca.helios5009.hyperion.core

import android.os.Build
import androidx.annotation.RequiresApi
import ca.helios5009.hyperion.core.misc.FileReader
import java.io.File
import java.nio.file.Paths

@RequiresApi(Build.VERSION_CODES.R)
class AutonPaths {
	val folder = FileReader().getFile("autonomous")
	fun storePath(content: String, fileName: String) {
		File("${folder}/${fileName}").writeText(content)
	}

	fun readPath(fileName: String): String {
		return File("${folder}/${fileName}").readText()
	}
//	fun getAllPaths(): List<String> {
//		return folder.toFile().listFiles()!!.map { it.name }
//	}
}