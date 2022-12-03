
plugins {
    id ("java")
    id ("org.jetbrains.kotlin.jvm") version "1.4.32"
}

group ="org.example"
version = "1.0-SNAPSHOT"
val codeEncoding = "UTF-8"


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
    javadoc { options.encoding = codeEncoding }
    compileJava { options.encoding = codeEncoding }
    compileTestJava { options.encoding = codeEncoding }
    test {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed")
        }
    }
}
sourceSets {
    main {
        java.srcDir("src/main/java")
    }
}
