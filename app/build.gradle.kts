plugins {
  java
  alias(libs.plugins.spotless)
}

group = "hu.bme.mit.ftsrg"

version = "0.1.0"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(11)) } }

repositories { mavenCentral() }

dependencies {
  implementation(libs.genson)
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
