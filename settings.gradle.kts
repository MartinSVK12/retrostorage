val modName: Provider<String> = providers.gradleProperty("mod_name")
rootProject.name = modName.get()
pluginManagement {
	fun isRepoHealthy(url: String): Boolean {
		var connection: javax.net.ssl.HttpsURLConnection? = null
		return try {
			connection = java.net.URI(url).toURL().openConnection() as javax.net.ssl.HttpsURLConnection
			connection.requestMethod = "HEAD"
			connection.connectTimeout = 2000
			connection.readTimeout = 2000
			connection.instanceFollowRedirects = true
			connection.connect()
			val code = connection.responseCode
			code in 200..399
		} catch (_: Exception) {
			false
		} finally {
			connection?.disconnect()
		}
	}
	fun repoUrlWithFallbacks(candidates: List<String>): String {
		if (candidates.isEmpty()) {
			val badLink = "https://mock.httpstatus.io/500"
			logger.error("No repositories have been provided. Defaulting to: {}", badLink)
			return badLink
		}
		val chosenRepository = candidates.firstOrNull { isRepoHealthy(it) } ?: run {
			if (candidates.size == 1) {
				logger.error("\"{}\" could not be resolved.", candidates.first())
			} else {
				logger.error("All {} repositories could not be resolved. Defaulting to: {}", candidates.size, candidates.first())
			}
			return candidates.first()
		}
		logger.lifecycle("Using \"{}\" as the Fabric repository.", chosenRepository)
		return chosenRepository
	}
	repositories {
		maven(
			repoUrlWithFallbacks(
				listOf(
					"https://maven.fabricmc.net",
					"https://maven2.fabricmc.net",
					"https://maven3.fabricmc.net"
				)
			)
		) { name = "Fabric" }
		maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
		mavenCentral()
		gradlePluginPortal()
	}
	val foojayResolverVersion = providers.gradleProperty("foojay_resolver_version")
	plugins {
		id("org.gradle.toolchains.foojay-resolver-convention").version(foojayResolverVersion.get())
	}
}
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention")
}

include(":catalyst")
include(":catalyst-all")
include(":catalyst-core")
include(":catalyst-effects")
include(":catalyst-fluids")
include(":catalyst-energy")
include(":catalyst-multiblocks")
//include(":catalyst-multipart")
include(":catalyst-screens")
project(":catalyst").projectDir = file("../catalyst")
project(":catalyst-all").projectDir = file("../catalyst/modules/all")
project(":catalyst-core").projectDir = file("../catalyst/modules/core")
project(":catalyst-effects").projectDir = file("../catalyst/modules/effects")
project(":catalyst-fluids").projectDir = file("../catalyst/modules/fluids")
project(":catalyst-energy").projectDir = file("../catalyst/modules/energy")
project(":catalyst-multiblocks").projectDir = file("../catalyst/modules/multiblocks")
//project(":catalyst-multipart").projectDir = file("../catalyst/modules/multipart")
project(":catalyst-screens").projectDir = file("../catalyst/modules/screens")

include(":tmb")
//include(":halplibe")
include(":btwaila")
project(":tmb").projectDir = file("../tmb")
project(":btwaila").projectDir = file("../btwaila")
//project(":halplibe").projectDir = file("../bta-halplibe")
