
plugins {
    id ("java")
    id ("org.jetbrains.kotlin.jvm") version "1.4.32"
}

group ="org.example"
version = "1.0-SNAPSHOT"

tasks.withType<Javadoc>() { options.encoding = "UTF-8" }

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    compileOnly ("org.projectlombok:lombok:1.18.24")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")

    testCompileOnly ("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.24")
}
tasks {
    javadoc {
        options.encoding = "UTF-8"
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
    test {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed")
        }
    }
}
