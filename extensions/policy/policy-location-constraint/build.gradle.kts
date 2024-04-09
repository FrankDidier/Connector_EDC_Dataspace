plugins {
    `java-library`
    id("application")
}

dependencies {
    api(project(":spi:data-plane:data-plane-spi"))

    implementation(project(":core:control-plane:control-plane-core"))
}
