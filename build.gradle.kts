plugins {
    java
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "com.sleapplugin"
version = "1.0.0"
description = "A plugin that allows night skip with half of online players"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paper {
    main = "com.sleapplugin.SleepPlugin"
    name = "SleepPlugin"
    version = "1.0.0"
    description = "A plugin that allows night skip with half of online players"
    apiVersion = "1.21"
    author = "Puer33"
    
    permissions {
        register("sleepplugin.admin") {
            description = "Allows access to sleep plugin admin commands"
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
        }
    }
}