package core.outport

import java.util.Properties

data class RandomPersonServiceConfig(
    val fetchUrl: String,
    val apiKey: String,
)

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val accessSecret: String,
    val refreshSecret: String
)

data class DeploymentConfig(
    val env: String,
    val version: String,
    val buildNumber: String,
)

interface GetDeploymentEnvPort {
    val deploymentEnv: String
}

interface GetDeploymentConfigPort {
    val deployment: DeploymentConfig
}

interface GetDatabaseConfigPort {
    val database: Properties
}

interface GetTokenConfigPort {
    val tokenConfig: TokenConfig
}

interface GetRandomPersonServiceConfigPort {
    val randomPersonService: RandomPersonServiceConfig
}
