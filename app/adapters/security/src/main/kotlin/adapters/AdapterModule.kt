package adapters

import core.outport.GenerateSaltedHashPort
import core.outport.VerifyPasswordPort
import org.koin.dsl.binds
import org.koin.dsl.module

val adapterModule = module {
    single {
        HashingAdapter()
    } binds arrayOf(
        VerifyPasswordPort::class,
        GenerateSaltedHashPort::class,
    )
}