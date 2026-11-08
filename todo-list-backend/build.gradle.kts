plugins {
    id("java")
    id("application")
    id("groovy")
}

group = "br.com.zghero"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("org.apache.groovy:groovy:4.0.24")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "br.com.zghero.ui.Main"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

