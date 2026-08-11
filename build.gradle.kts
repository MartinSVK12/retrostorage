import com.google.gson.Gson
import com.smushytaco.lwjgl_gradle.Preset
import groovy.namespace.QName
import groovy.util.Node
import groovy.xml.XmlParser
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL

plugins {
	alias(libs.plugins.loom)
	alias(libs.plugins.lwjgl)
	alias(libs.plugins.minotaur)
	java
	`maven-publish`
}
val modVersion: String = project.properties["mod_version"].toString()
val modGroup: String = project.properties["mod_group"].toString()
val modName: String = project.properties["mod_name"].toString()

val lib = extensions.getByType<VersionCatalogsExtension>().named("libs")
val javaVersion: Provider<Int> = libs.versions.java.map { it.toInt() }

base.archivesName = modName
group = modGroup
version = modVersion

class AccountsJson(val accounts: List<Account>)
class Account(val profile: Profile, val ygg: YGG)
class YGG(val token: String)
class Profile(val name: String, val id: String)

val prismAccountsFile = providers.provider {
	val explicit = providers.gradleProperty("prism.accounts.file").orNull
	if (explicit != null) {
		val explicitFile = File(explicit)
		if (explicitFile.exists()) return@provider explicitFile
	}

	val home = System.getProperty("user.home")

	val candidates = buildList {
		// Windows
		System.getenv("APPDATA")?.let { add(File(it, "PrismLauncher/accounts.json")) }
		System.getenv("HOMEPATH")?.let { add(File(it, "scoop/persist/prismlauncher/accounts.json")) }
		// Linux / XDG
		val xdgDataHome = System.getenv("XDG_DATA_HOME")
		if (xdgDataHome != null) {
			add(File(xdgDataHome, "PrismLauncher/accounts.json"))
		} else {
			add(File(home, ".local/share/PrismLauncher/accounts.json"))
		}
		// Flatpak
		add(File(home, ".var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/accounts.json"))
		// macOS
		add(File(home, "Library/Application Support/PrismLauncher/accounts.json"))
	}
	candidates.firstOrNull(File::exists)
}

loom {
	customMinecraftMetadata.set("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/${libs.versions.bta.get()}/manifest.json")
	//accessWidenerPath.set(file("src/main/resources/signalindustries.classtweaker"))
	runs {
		prismAccountsFile.orNull?.let { file ->
			val account: Provider<Account> = providers.fileContents(layout.file(providers.provider { file }))
				.asText
				.map { jsonStr ->
					val accountNumber = (providers.gradleProperty("prism.accounts.number").orNull?.toInt() ?: 1) - 1
					val accounts = Gson().fromJson(jsonStr, AccountsJson::class.java).accounts
					accounts.getOrNull(accountNumber.coerceIn(0, accounts.size - 1))
						?: error("No PrismLauncher accounts found in ${file.absolutePath}")
				}
			register("clientAuth") {
				inherit(getByName("client"))
				configName = "Minecraft Client (Auth)"
				val acc = account.get()
				programArgs("--username", acc.profile.name, "--uuid", acc.profile.id, "--session", acc.ygg.token)
			}
		}
	}
}
repositories {
	mavenCentral()
	maven("https://maven.fabricmc.net/") { name = "Fabric" }
	maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
	maven("https://maven.thesignalumproject.net/releases") { name = "SignalumMavenReleases" }
	maven("https://maven.thesignalumproject.net/nightly") { name = "SignalumMavenNightly" }
	maven("https://maven.danygames2014.net/signalum") { name = "SignalumMavenMirror1" }
	ivy("https://github.com/Turnip-Labs") {
		patternLayout {
			artifact("/fabric-loader/releases/download/[revision]/fabric-loader-[revision].jar")
		}
		metadataSources { artifact() }
		content { includeGroup("bta.loader") }
	}
	ivy("https://github.com/Better-than-Adventure") {
		patternLayout { artifact("[organisation]/releases/download/[revision]/[module]-bta-[revision].jar") }
		metadataSources { artifact() }
	}
	ivy("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/") {
		patternLayout { artifact("/v[revision]/client.jar") }
		metadataSources { artifact() }
	}
	ivy("https://downloads.betterthanadventure.net/bta-server/${libs.versions.btaChannel.get()}/") {
		patternLayout { artifact("/v[revision]/server.jar") }
		metadataSources { artifact() }
	}
	ivy("https://piston-data.mojang.com") {
		patternLayout { artifact("v1/[organisation]/[revision]/[module].jar") }
		metadataSources { artifact() }
	}
}
lwjgl {
	version = libs.versions.lwjgl
	implementation(Preset.MINIMAL_OPENGL)
}
dependencies {
	minecraft("::${libs.versions.bta.get()}")

	runtimeOnly(libs.clientJar)
	implementation(libs.loader)
	// If you do not need Halplibe you can comment out or delete this line.
	implementation(libs.halplibe)
	implementation(libs.modMenu)
	implementation(libs.legacyLwjgl)

	implementation(libs.slf4jApi)
	implementation(libs.guava)
	implementation(libs.log4j.slf4j2.impl)
	implementation(libs.log4j.core)
	implementation(libs.log4j.api)
	implementation(libs.log4j.api12)
	implementation(libs.gson)

	implementation(libs.commonsLang3)
	include(libs.commonsLang3)

	//implementation(project(":catalyst-all"))
	implementation(project(":catalyst-core"))
	implementation(project(":catalyst-fluids"))
	implementation(project(":catalyst-energy"))
	implementation(project(":catalyst-multiblocks"))
	//implementation(project(":catalyst-multipart"))
	implementation(project(":catalyst-effects"))
	implementation(project(":catalyst-screens"))
	implementation(project(":tmb"))
	implementation(project(":btwaila"))
}

subprojects {
	apply(plugin = "java")

	repositories {
		mavenCentral()
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
		maven("https://maven.thesignalumproject.net/releases") { name = "SignalumMavenReleases" }
		maven("https://maven.thesignalumproject.net/nightly") { name = "SignalumMavenNightly" }
		ivy("https://github.com/Better-than-Adventure") {
			patternLayout { artifact("[organisation]/releases/download/[revision]/[module]-bta-[revision].jar") }
			metadataSources { artifact() }
		}
		ivy("https://downloads.betterthanadventure.net/bta-client/${lib.findVersion("btaChannel").get()}/") {
			patternLayout { artifact("/v[revision]/client.jar") }
			metadataSources { artifact() }
		}
		ivy("https://downloads.betterthanadventure.net/bta-server/${lib.findVersion("btaChannel").get()}/") {
			patternLayout { artifact("/v[revision]/server.jar") }
			metadataSources { artifact() }
		}
		ivy("https://piston-data.mojang.com") {
			patternLayout { artifact("v1/[organisation]/[revision]/[module].jar") }
			metadataSources { artifact() }
		}
	}

	dependencies {
		runtimeOnly(lib.findLibrary("clientJar").get())
		implementation(lib.findLibrary("loader").get())
		implementation(lib.findLibrary("halplibe").get())
		implementation(lib.findLibrary("modMenu").get())
		implementation(lib.findLibrary("legacyLwjgl").get())

		implementation(lib.findLibrary("slf4jApi").get())
		implementation(lib.findLibrary("guava").get())
		implementation(lib.findLibrary("log4j.slf4j2.impl").get())
		implementation(lib.findLibrary("log4j.core").get())
		implementation(lib.findLibrary("log4j.api").get())
		implementation(lib.findLibrary("log4j.api12").get())
		implementation(lib.findLibrary("gson").get())

		implementation(lib.findLibrary("commonsLang3").get())
	}
}

java {
	toolchain {
		languageVersion = javaVersion.map { JavaLanguageVersion.of(it) }
		vendor = JvmVendorSpec.ADOPTIUM
	}
	sourceCompatibility = JavaVersion.toVersion(javaVersion.get())
	targetCompatibility = JavaVersion.toVersion(javaVersion.get())
	withSourcesJar()
}
val licenseFile = run {
	val rootLicense = layout.projectDirectory.file("LICENSE")
	val parentLicense = layout.projectDirectory.file("../LICENSE")
	when {
		rootLicense.asFile.exists() -> {
			logger.lifecycle("Using LICENSE from project root: {}", rootLicense.asFile)
			rootLicense
		}
		parentLicense.asFile.exists() -> {
			logger.lifecycle("Using LICENSE from parent directory: {}", parentLicense.asFile)
			parentLicense
		}
		else -> {
			logger.warn("No LICENSE file found in project or parent directory.")
			null
		}
	}
}
tasks {
	withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
		sourceCompatibility = javaVersion.get().toString()
		targetCompatibility = javaVersion.get().toString()
		if (javaVersion.get() > 8) options.release = javaVersion
	}
	if(rootProject == this) {
		named<UpdateDaemonJvm>("updateDaemonJvm") {
			languageVersion = libs.versions.gradleJava.map { JavaLanguageVersion.of(it.toInt()) }
			vendor = JvmVendorSpec.ADOPTIUM
		}
	}
	withType<JavaExec>().configureEach { defaultCharacterEncoding = "UTF-8" }
	withType<Javadoc>().configureEach { options.encoding = "UTF-8" }
	withType<Test>().configureEach { defaultCharacterEncoding = "UTF-8" }
	withType<Jar>().configureEach {
		licenseFile?.let {
			from(it) {
				rename { original -> "${original}_${archiveBaseName.get()}" }
			}
		}
	}
	processResources {
		val resourceMap = mapOf(
			"version" to modVersion,
			"loader" to libs.versions.loader.get(),
			"halplibe" to libs.versions.halplibe.get(),
			"java" to libs.versions.java.get(),
			"modmenu" to libs.versions.modMenu.get(),
			"core" to project(":catalyst-core").properties["mod_version"] as String,
			"fluids" to project(":catalyst-fluids").properties["mod_version"] as String,
			"energy" to project(":catalyst-energy").properties["mod_version"] as String,
			"multiblocks" to project(":catalyst-multiblocks").properties["mod_version"] as String,
			//"multipart" to project(":catalyst-multipart").properties["mod_version"] as String,
			"effects" to project(":catalyst-effects").properties["mod_version"] as String,
			"screens" to project(":catalyst-screens").properties["mod_version"] as String
		)
		inputs.properties(resourceMap)
		filesMatching("fabric.mod.json") { expand(resourceMap) }
		filesMatching("**/*.mixins.json") { expand(resourceMap.filterKeys { it == "java" }) }
	}
}
// Removes LWJGL2 dependencies
configurations.configureEach { exclude(group = "org.lwjgl.lwjgl") }

publishing {
	if(checkVersion(modGroup, modName, modVersion)){
		repositories {
			maven {
				name = "signalumMaven"
				url = uri("https://maven.thesignalumproject.net/releases")
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}

			publications {
				create<MavenPublication>("maven") {
					groupId = project.property("mod_group").toString()
					artifactId = project.property("mod_name").toString()
					version = project.property("mod_version").toString()
					from(components["java"])
				}
			}
		}
	}
}

fun checkVersion(group: String, name: String, version: String): Boolean {
	return !(rootProject.property("check_versions") as String).toBoolean() || try {
		val xml = URL("https://maven.thesignalumproject.net/releases/$group/$name/maven-metadata.xml").readText()
		val metadata = XmlParser().parseText(xml)

		val versions = metadata.getAt(QName("versioning")).getAt("versions").getAt("version").map { (it as Node).text() }

		if (version in versions) {
			System.err.println("Version $version of $group.$name already exists!")
			false
		} else {
			true
		}
	} catch (e: IOException) {
		System.err.println(e.message)
		true
	}
}
