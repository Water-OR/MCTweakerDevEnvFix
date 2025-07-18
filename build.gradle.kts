plugins {
    `java-library`
}

group = "net.llvg"
version = property("version") as String
base.archivesName = "MCTweakerDevEnvFix"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.1")
    compileOnly("com.google.guava:guava:33.4.8-jre")
}

tasks {
    withType<Jar> {
        exclude("net/minecraft/**")
        exclude("net/minecraftforge/**")
    }
    
    jar {
        manifest.attributes("FMLCorePlugin" to "net.llvg.dev_tweaker_fix.DevTweakerFix")
    }
    
    javadoc {
        exclude("net/minecraft/**")
        exclude("net/minecraftforge/**")
    }
}