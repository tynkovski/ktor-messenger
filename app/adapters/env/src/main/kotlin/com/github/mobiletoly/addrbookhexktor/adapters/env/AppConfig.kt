package com.github.mobiletoly.addrbookhexktor.adapters.env

import com.github.mobiletoly.addrbookhexktor.outport.DeploymentConfig
import com.github.mobiletoly.addrbookhexktor.outport.GetDatabaseConfigPort
import com.github.mobiletoly.addrbookhexktor.outport.GetDeploymentConfigPort
import com.github.mobiletoly.addrbookhexktor.outport.GetRandomPersonServiceConfigPort
import com.github.mobiletoly.addrbookhexktor.outport.RandomPersonServiceConfig
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Properties

internal class AppConfig(deploymentEnv: String) :
    GetDeploymentConfigPort,
    GetDatabaseConfigPort,
    GetRandomPersonServiceConfigPort {

    private val config: Config

    init {
        val envConfig = ConfigFactory.load("config-$deploymentEnv.conf")
        val commonConf = ConfigFactory.load("config-common.conf")
        val rootConfig = ConfigFactory
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

    override val randomPersonService: RandomPersonServiceConfig by lazy {
        val node = config.getConfig("random-person-service")
        RandomPersonServiceConfig(
            fetchUrl = node.getString("fetch-url"),
            apiKey = node.getString("api-key")
        )
    }

    private fun Config.toProperties() = Properties().also {
        for (e in this.entrySet()) {
            it.setProperty(e.key, this.getString(e.key))
        }
    }
}
