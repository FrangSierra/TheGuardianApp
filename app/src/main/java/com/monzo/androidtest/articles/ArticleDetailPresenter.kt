package com.monzo.androidtest.articles

import com.monzo.androidtest.articles.domain.ArticlesController
import com.monzo.androidtest.articles.model.Article
import com.monzo.androidtest.common.BasePresenter
import com.monzo.androidtest.common.BasePresenterView
import io.reactivex.Observable
import io.reactivex.Scheduler

class ArticleDetailPresenter(private val uiScheduler: Scheduler,
                             private val articlesController: ArticlesController) : BasePresenter<ArticleDetailPresenter.View>() {

    override fun register(view: View) {
        super.register(view)

        view.loadArticleAction()
                .doOnNext { view.showLoadingProgressBar(true) }
                .switchMapSingle { url -> articlesController.getArticle(url) }
                .observeOn(uiScheduler)
                .subscribe({ article ->
                    view.showLoadingProgressBar(false)
                    view.showArticle(article)
                }, { view.onError() })
                .addToUnsubscribe()
    }

    interface View : BasePresenterView {
        fun loadArticleAction(): Observable<String>
        fun showLoadingProgressBar(show: Boolean)

        fun showArticle(article: Article)
        fun onError() //TODO customize based on error
    }
}