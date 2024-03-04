import adapters.adapterModule
import org.koin.dsl.module
import service.serviceModule

val securityModule = module {
    includes(serviceModule, adapterModule)
}