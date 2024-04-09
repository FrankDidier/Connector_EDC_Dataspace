plugins {
    `java-library`
    id("application")
}

dependencies {
    api(project(":spi:common:auth-spi"))
    api(project(":spi:common:policy-engine-spi"))
    api(project(":spi:control-plane:contract-spi"))

    testImplementation(project(":core:common:junit"))

    testImplementation(libs.mockito.core)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.params)
}
