plugins {
  java
  alias(libs.plugins.spotless)
  alias(libs.plugins.shadow)
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://jitpack.io")
  }
}

dependencies {
  implementation(libs.fabric.chaincode.shim)

  testImplementation(libs.junit.jupiter)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.junit.jupiter)
  testRuntimeOnly(libs.junit.platform.launcher)
}

java { toolchain { languageVersion = JavaLanguageVersion.of(11) } }

tasks.named<Test>("test") {
  useJUnitPlatform()
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
