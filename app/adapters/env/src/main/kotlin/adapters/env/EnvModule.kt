package adapters.env

import core.outport.*
import org.koin.dsl.binds
import org.koin.dsl.module

val envModule =
    module {
        single<GetDeploymentEnvPort> {
            AppOsEnvironment()
        }

        single {
            HoconBasedAppConfig(get<GetDeploymentEnvPort>().deploymentEnv)
        } binds
            arrayOf(
                GetDeploymentConfigPort::class,
                GetDatabaseConfigPort::class,
                GetRandomPersonServiceConfigPort::class,
                GetAuthPort::class,
            )
    }
