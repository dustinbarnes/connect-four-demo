plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.12.0"
}

group = "com.github.dustinbarnes.connect_four_demo"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jooq:jooq:3.20.0")
	implementation("org.xerial:sqlite-jdbc:3.45.3.0")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.22")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("${rootDir}/../openapi/auth.yaml")
	outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)
	apiPackage.set("com.github.dustinbarnes.connect_four_demo.backend.api")
	modelPackage.set("com.github.dustinbarnes.connect_four_demo.backend.model")
	invokerPackage.set("com.github.dustinbarnes.connect_four_demo.backend.invoker")
	configOptions.set(mapOf(
		"interfaceOnly" to "true",
		"useTags" to "true",
		"dateLibrary" to "java8",
		"useSpringBoot3" to "true",
		"generateSpringSecurity" to "true",
	))
}
sourceSets["main"].java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/java"))

tasks.withType<Test> {
	useJUnitPlatform()
}
