package com.monzo.androidtest.articles.domain

import com.monzo.androidtest.articles.ArticlesRepository
import com.monzo.androidtest.articles.model.Article
import io.reactivex.Scheduler
import io.reactivex.Single

interface ArticlesController {
    fun searchArticles(resetPagination: Boolean): Single<List<Article>>
    fun getArticle(url: String): Single<Article>
}

class ArticlesControllerImpl(private val ioScheduler: Scheduler,
                             private val repository: ArticlesRepository) : ArticlesController {
    private var currentPageIndex = 0L

    override fun searchArticles(resetPagination: Boolean): Single<List<Article>> {
        if (resetPagination) currentPageIndex = 0L

        currentPageIndex++
        return repository.latestArticles(currentPageIndex)
                .subscribeOn(ioScheduler)
                .map { articles ->
                    articles.sortedByDescending { it.published }
                            .filter { it.hasExtraFields() }
                }
    }

    override fun getArticle(url: String): Single<Article> {
        return repository.getArticle(url)
                .subscribeOn(ioScheduler)
    }
}