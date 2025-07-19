plugins {
    `java-library`
}

group = "net.llvg"
version = property("version") as String
base.archivesName = "MCTweakerDevEnvFix"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
    
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net") {
        name = "Minecraft maven"
        content {
            includeGroup("net.minecraft")
        }
    }
}

dependencies {
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.1")
    compileOnly("com.google.guava:guava:33.4.8-jre")
    compileOnly("net.minecraft:launchwrapper:1.12") {
        exclude(group = "org.apache.logging.log4j")
    }
}

tasks {
    withType<Jar> {
        exclude("net/minecraftforge/**")
    }
    
    jar {
        manifest.attributes("FMLCorePlugin" to "net.llvg.dev_tweaker_fix.DevTweakerFix")
    }
    
    withType<Javadoc> {
        exclude("net/minecraftforge/**")
    }
}