package com.monzo.androidtest.articles

import com.monzo.androidtest.articles.domain.ArticlesController
import com.monzo.androidtest.articles.domain.ArticlesControllerImpl
import com.monzo.testdata.TestData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

import org.mockito.MockitoAnnotations.initMocks
import strikt.api.expectThat
import strikt.assertions.hasSize

class ArticlesControllerTest {
    private lateinit var controller: ArticlesController

    @Test
    @Throws(Exception::class)
    fun `controller removes articles without thumbnail`() {
        val mockRepository = mock<ArticlesRepository> {
            this.on(mock.latestArticles(1)).doReturn(Single.just(TestData.sampleArticlesList))
        }
        controller = ArticlesControllerImpl(Schedulers.trampoline(), mockRepository)
        val sortedArticles = controller.searchArticles(true).blockingGet()
        expectThat(sortedArticles) {
            hasSize(1)
        }
    }
}