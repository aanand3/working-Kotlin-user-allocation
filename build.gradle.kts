import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.moowork.gradle.node.yarn.YarnTask

plugins {
	id("org.springframework.boot") version "2.6.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.github.node-gradle.node") version "2.2.0"
	kotlin("jvm") version "1.6.0"
	kotlin("plugin.spring") version "1.6.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

node {
	version = "16.13.1"
	download = true
}

task("installDependencies", YarnTask::class) {
	args = mutableListOf("install", "--network-timeout", "10000")
	setExecOverrides(closureOf<ExecSpec> {
		setWorkingDir("./src/ts")
	})
}

task("buildWeb", YarnTask::class) {
	args = mutableListOf("build")
	setExecOverrides(closureOf<ExecSpec> {
		setWorkingDir("./src/ts")
	})
}

//project.tasks["testWeb"].dependsOn("installDependencies")
project.tasks["buildWeb"].dependsOn("installDependencies")

//project.tasks["test"].dependsOn("testWeb")
project.tasks["bootRun"].dependsOn("buildWeb")
project.tasks["bootJar"].dependsOn("buildWeb")
