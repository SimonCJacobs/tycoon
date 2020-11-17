package jacobs.tycoon.application

import org.kodein.di.Kodein
import org.kodein.di.bindings.NoArgSimpleBindingKodein
import org.kodein.di.bindings.Singleton
import org.kodein.di.erased.singleton

enum class ApplicationMode {
    NORMAL, ADMIN
}

inline fun < C, reified T : Any > Kodein.BindBuilder.WithScope < C >.singletonsByMode(
    mode: ApplicationMode,
    singletonsClosure: SingletonsBuilder < C, T >.() -> Unit
): Singleton<C, T> {
    val singletonsBuilder = SingletonsBuilder < C, T >().apply { singletonsClosure() }
    val singletonProvider =
        if ( mode == ApplicationMode.ADMIN )
            singletonsBuilder.admin
        else
            singletonsBuilder.normal
    return singleton { singletonProvider() }
}

class SingletonsBuilder < C, T : Any > {
    lateinit var admin: NoArgSimpleBindingKodein<C>.() -> T
    lateinit var normal: NoArgSimpleBindingKodein<C>.() -> T
}