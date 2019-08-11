package com.monzo.androidtest.views

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.ObservableEmitter

/**
 * Implementation of [EndlessScroller] for [RecyclerView].
 */
class RecyclerViewEndlessScroller(loadProcessor: ObservableEmitter<Any>) : EndlessScroller<RecyclerView>(loadProcessor) {
    private val scrollListener: RecyclerView.OnScrollListener
    private var layoutManager: RecyclerView.LayoutManager? = null

    init {
        this.scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoading) {
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager!!.itemCount
                    val firstVisibleItem = firstVisibleItemPosition(layoutManager!!)
                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        loadStarted()
                    }
                }
            }
        }
    }

    /* Internal methods */
    private fun firstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        return if (layoutManager is GridLayoutManager) {
            layoutManager.findFirstCompletelyVisibleItemPosition()
        } else /*if (layoutManager instanceof LinearLayoutManager)*/ {
            (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        }
    }

    /* EndlessScroller */
    override fun onAttach(view: RecyclerView) {
        layoutManager = view.layoutManager
        view.addOnScrollListener(scrollListener)
    }

    override fun onDetach(view: RecyclerView) {
        layoutManager = null
        view.removeOnScrollListener(scrollListener)
    }
}