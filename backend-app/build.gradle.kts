plugins {
    java
    idea
    id("com.adarshr.test-logger") version Versions.gradleTestLoggerPlugin
    id("io.micronaut.application") version Versions.gradleMicronautPlugin
}

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = false
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()
    }
}

micronaut {
    version(Versions.micronaut)
    processing {
        incremental(true)
        module(project.name)
        group(project.group.toString())
        annotations("com.zhokhov.jiva.challenge.*")
    }
}

dependencies {
    // Micronaut
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut:micronaut-management")
    testImplementation("io.micronaut.test:micronaut-test-junit5")

    // libraries
    implementation("ch.qos.logback:logback-classic")
    implementation("javax.annotation:javax.annotation-api")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClassName = "com.zhokhov.jiva.challenge.server.JivaServerApplication"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines = setOf("junit-jupiter")
        excludeEngines = setOf("junit-vintage")
    }
}
