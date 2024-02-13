package ca.helios5009.hyperion.core.misc

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
class FileReader {
	fun getFile(path: String): String {
		return Environment.getStorageDirectory().absolutePath + "/self/primary/Hyperion/" + path
	}
}