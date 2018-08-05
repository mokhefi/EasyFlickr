package com.themasterspirit.easyflickr.di

import org.kodein.di.Kodein
import org.kodein.di.KodeinContainer
import org.kodein.di.KodeinTree

class AppKodeinContainer : KodeinContainer {

    override val tree: KodeinTree
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun <C, A, T : Any> allFactories(key: Kodein.Key<C, A, T>, context: C, receiver: Any?, overrideLevel: Int): List<(A) -> T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <C, A, T : Any> factory(key: Kodein.Key<C, A, T>, context: C, receiver: Any?, overrideLevel: Int): (A) -> T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <C, A, T : Any> factoryOrNull(key: Kodein.Key<C, A, T>, context: C, receiver: Any?, overrideLevel: Int): ((A) -> T)? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}