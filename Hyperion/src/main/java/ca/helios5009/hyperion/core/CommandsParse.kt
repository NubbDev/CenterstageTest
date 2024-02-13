package ca.helios5009.hyperion.core

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

@SuppressLint("UnsafeDynamicallyLoadedCode")
@RequiresApi(Build.VERSION_CODES.O)
class CommandsParse {
	init {
		System.load("hyperionlib")
	}

	external fun read(commands: String): String

}