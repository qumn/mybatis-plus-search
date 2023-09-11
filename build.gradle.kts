plugins {
    kotlin("jvm") version "1.9.0"
}

group = "xyz.qumn"
version = "1.0-SNAPSHOT"
val kotestVersion = "5.7.2"
val h2Version = "2.2.222"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    implementation("com.baomidou:mybatis-plus:3.5.3.2")
    // because the mybatis dependent on mybatis plus
    implementation("org.springframework:spring-core:5.3.29")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}