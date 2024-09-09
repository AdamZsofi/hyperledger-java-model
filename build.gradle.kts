plugins {
  id("java")
  id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.20"
  id("com.diffplug.spotless") version "6.25.0"
}

group = "org.example"

version = "1.0-SNAPSHOT"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(11)) } }

repositories { mavenCentral() }

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("com.owlike:genson:1.6")
}

tasks.test { useJUnitPlatform() }

tasks.withType<JavaCompile> {
  options.compilerArgs.add("-Aproject=${project.group}/${project.name}")
}

spotless {
  ratchetFrom("origin/main")

  format("misc") {
    target(".gitignore")
    trimTrailingWhitespace()
    indentWithTabs()
    endWithNewline()
  }

  java {
    importOrder()
    removeUnusedImports()
    googleJavaFormat()
    formatAnnotations()
    toggleOffOn()
    licenseHeader("/* SPDX-License-Identifier: Apache-2.0 */")
  }

  kotlinGradle { ktfmt() }
}
