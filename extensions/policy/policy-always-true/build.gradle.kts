plugins {
    `java-library`
    id("application")
}

dependencies {
    api(project(":spi:common:core-spi"))
    api(project(":spi:common:policy-engine-spi"))
    api(project(":spi:control-plane:control-plane-spi"))
    implementation(project(":extensions:common:api:api-core"))

    testImplementation(project(":core:common:junit"))
    testImplementation(project(":core:control-plane:control-plane-core"))
    testImplementation(project(":core:data-plane-selector:data-plane-selector-core"))
    testImplementation(libs.mockito.core)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
