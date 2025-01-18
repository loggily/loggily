package com.loggily.loggily.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "loggily-config")
data class LoggilyPropertyConfig(
    val logOrigin: LogOrigin = LogOrigin(),
)

data class LogOrigin(
    val environmentKey: String = "k8s.namespace.name",
    val hostKey: String = "k8s.pod.name",
    val applicationKey: String = "k8s.container.name"
)