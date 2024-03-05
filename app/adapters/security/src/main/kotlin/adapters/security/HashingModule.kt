package adapters.security

import core.outport.GenerateSaltedHashPort
import core.outport.VerifyPasswordPort
import org.koin.dsl.binds
import org.koin.dsl.module

val hashingModule = module {
    single {
        HashingAdapter()
    } binds arrayOf(
        VerifyPasswordPort::class,
        GenerateSaltedHashPort::class,
    )
}