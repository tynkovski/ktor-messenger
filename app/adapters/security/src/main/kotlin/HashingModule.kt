import adapters.adapterModule
import org.koin.dsl.module

val hashingModule = module {
    includes(adapterModule)
}