import com.android.build.gradle.tasks.MergeSourceSetFolders
import com.nishtahir.CargoBuildTask
import com.nishtahir.CargoExtension

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.mozilla.rust-android-gradle.rust-android") version "0.9.3"
}

//apply("../build.common.gradle")
//apply("../build.dependencies.gradle")

android {
	namespace = "ca.helios5009.hyperion"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}

	ndkVersion = "26.1.10909125"
}

extensions.configure(CargoExtension::class.java) {
	module = "./src/main/rust"
	libname = "hyperion"
	targets = listOf(
		"arm","arm64"
	)

}

tasks.preBuild.configure {
	dependsOn.add(tasks.withType(CargoBuildTask::class.java))
}

dependencies {
	compileOnly("org.firstinspires.ftc:RobotCore:9.0.1")
	compileOnly("org.firstinspires.ftc:Hardware:9.0.1")
	compileOnly("org.firstinspires.ftc:FtcCommon:9.0.1")

	implementation("androidx.core:core-ktx:1.2.0")
	implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.6.10"))
//	implementation("com.google.code.gson:gson:2.8.9")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
}

project.afterEvaluate{
	for (buildTask in tasks.withType(CargoBuildTask::class.java)) {
		tasks.withType(MergeSourceSetFolders::class.java).configureEach {
			this.inputs.dir(layout.buildDirectory.dir("rustJniLibs" + File.separator + buildTask.toolchain!!.folder))
			this.dependsOn(buildTask)
		}
	}
}