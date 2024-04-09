plugins {
    `java-library`
    id("application")
}

dependencies {
    api(project(":spi:common:auth-spi"))
    api(project(":spi:common:policy-engine-spi"))
}
