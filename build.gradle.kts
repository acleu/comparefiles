plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" // create uber jar

    // Use "gradle nativeCompile" to create a native binary with graal native-image.
    // For details, see https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html
    id("org.graalvm.buildtools.native") version "0.9.28"
}

group = "com.als"
version = "1.0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass.set("com.als.comparefiles.CompareFiles")
}

dependencies {

    implementation("info.picocli:picocli:4.7.5")
    annotationProcessor("info.picocli:picocli-codegen:4.7.5")

//    implementation("org.apache.commons:commons-imaging:1.0-SNAPSHOT")
//    implementation("net.coobird:thumbnailator:0.4.8")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.jetbrains:annotations:24.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Aproject=${project.group}/${project.name}")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
