import java.io.ByteArrayOutputStream

plugins {
    java
    checkstyle
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "net.eaglefamily"
version = version()

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/BlackEagleEF/playlegend-test-task")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")

    // https://mvnrepository.com/artifact/io.reactivex.rxjava3/rxjava
    implementation("io.reactivex.rxjava3:rxjava:3.1.2")

    implementation("org.jooq:jooq:3.15.4")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.flywaydb:flyway-core:8.0.2")
    implementation("org.postgresql:postgresql:42.3.1")

    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(tasks["shadowJar"])
            from(components["java"])
        }
    }
    repositories {
        mavenLocal {}
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/" + System.getenv("GITHUB_REPOSITORY"))
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

checkstyle {
    toolVersion = "9.0.1"
}

fun version(): String {
    val out = ByteArrayOutputStream()
    exec {
        isIgnoreExitValue = true
        setCommandLine("git", "describe", "--tags", "--always", "--first-parent")
        standardOutput = out
    }

    return if (out.size() == 0) {
        "unknown"
    } else {
        out.toString("UTF-8").trim()
    }
}

val tokens = mapOf(
    "NAME" to rootProject.name,
    "PACKAGE_NAME" to project.name.replace("-", ""),
    "GROUP" to project.group,
    "VERSION" to project.version
)
tasks.withType<Jar> {
    filesMatching("*.yml") {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokens)
    }
}
