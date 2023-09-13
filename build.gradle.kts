plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
    signing
}

group = "io.github.qumn"
version = "1.6-beta"
val kotestVersion = "5.7.2"
val h2Version = "2.2.222"
val springVersion = "5.3.29"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("com.h2database:h2:$h2Version")
    implementation("com.baomidou:mybatis-plus:3.5.3.2")
    // because the mybatis dependent on mybatis plus
    implementation("org.springframework:spring-core:$springVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.register<Jar>("javadocJar") {
    from(tasks["javadoc"])
    archiveClassifier.set("javadoc")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(tasks["javadocJar"]) {
                classifier = "javadoc"
            }

            pom {
                name.set("mybatis plus search")
                description.set("a useful tool for mybatis plus search")
                url.set("https://github.com/qumn/mybatis-plus-search")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("qumn")
                        name.set("Wang ZiJian")
                        email.set("2476573497@qq.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/qumn/mybatis-plus-search.git")
                    developerConnection.set("scm:git:ssh://github.com/qumn/mybatis-plus-search.git")
                    url.set("https://github.com/qumn/mybatis-plus-search")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USERNAME") ?: project.findProperty("OSSRH_USERNAME") as String
                password = System.getenv("OSSRH_PASSWORD") ?: project.findProperty("OSSRH_PASSWORD") as String
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}