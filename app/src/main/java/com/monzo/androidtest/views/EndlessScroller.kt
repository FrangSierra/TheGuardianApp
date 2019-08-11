package com.monzo.androidtest.views

import android.view.View
import io.reactivex.ObservableEmitter

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Base class to implement 'endless scrolling' pattern.
 * <br></br>
 * @param <T> Type of the scrollable view.
</T> */
abstract class EndlessScroller<T : View>(private val loadEmitter: ObservableEmitter<Any>) {
    private val loading = AtomicBoolean()
    private var view: T? = null

    /**
     * Returns if the scroller is attached.
     */
    private val isAttached: Boolean
        get() = view != null

    /**
     * Returns if the scroller is loading
     */
    val isLoading: Boolean
        get() = loading.get()

    protected fun loadStarted() {
        loading.set(true)
        if (!loadEmitter.isDisposed)
            loadEmitter.onNext(Unit)
    }

    /* Methods to override in subclasses */
    protected abstract fun onAttach(view: T)

    protected abstract fun onDetach(view: T)

    /* EndlessScroller */

    /**
     * Attaches scroller to a view.
     */
    fun attachTo(view: T): EndlessScroller<T> {
        if (this.view === view) return this
        detach()
        this.view = view
        onAttach(view)
        return this
    }

    /**
     * Detaches the scroller from the view.
     */
    private fun detach() {
        if (isAttached) {
            onDetach(view!!)
            view = null
        }
        loadFinished()
    }

    /**
     * Sets scroller load as finished.
     */
    fun loadFinished() {
        loading.set(false)
    }
}