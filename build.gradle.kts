import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.flywaydb.flyway") version "10.7.2"
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	jacoco
	id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

flyway {
	url = "jdbc:mysql://localhost:8806/its"
	user = "root"
	password = "123456"
	connectRetries = 10
}

group = "com.firsteducation.marsladder"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.apache.tinkerpop:gremlin-driver:3.5.3")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.codehaus.groovy:groovy:3.0.8")

	runtimeOnly("org.flywaydb:flyway-core")
	runtimeOnly("org.flywaydb:flyway-mysql")
	runtimeOnly("com.mysql:mysql-connector-j:8.3.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
