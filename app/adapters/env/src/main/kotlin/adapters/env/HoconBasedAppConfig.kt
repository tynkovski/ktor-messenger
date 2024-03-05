package adapters.env

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import core.outport.*
import java.util.*

/**
 * Application configuration reader from HOCON config file.
 */
internal class HoconBasedAppConfig(deploymentEnv: String) :
    GetDeploymentConfigPort,
    GetDatabaseConfigPort,
    GetRandomPersonServiceConfigPort,
    GetTokenConfigPort
{
    private val config: Config

    init {
        val envConfig = ConfigFactory.load("config-$deploymentEnv.conf")
        val commonConf = ConfigFactory.load("config-common.conf")
        val rootConfig =
            ConfigFactory
                .load()
                .withFallback(envConfig)
                .withFallback(commonConf)
                .resolve()
        this.config = rootConfig.getConfig("app-config")
    }

    override val deployment: DeploymentConfig by lazy {
        val node = config.getConfig("deployment")
        DeploymentConfig(
            env = node.getString("env"),
            version = node.getString("version"),
            buildNumber = node.getString("buildNumber"),
        )
    }

    override val database: Properties by lazy {
        val node = config.getConfig("main-db.hikari")
        node.toProperties()
    }

    override val tokenConfig: TokenConfig by lazy {
        val node = config.getConfig("jwt")
        TokenConfig(
            realm = node.getString("realm"),
            issuer = node.getString("issuer"),
            audience = node.getString("audience"),
            accessSecret = node.getString("accessSecret"),
            refreshSecret = node.getString("refreshSecret"),
        )
    }

    override val randomPersonService: RandomPersonServiceConfig by lazy {
        val node = config.getConfig("random-person-service")
        RandomPersonServiceConfig(
            fetchUrl = node.getString("fetch-url"),
            apiKey = node.getString("api-key"),
        )
    }

    private fun Config.toProperties() =
        Properties().also {
            for (e in this.entrySet()) {
                it.setProperty(e.key, this.getString(e.key))
            }
        }
}
