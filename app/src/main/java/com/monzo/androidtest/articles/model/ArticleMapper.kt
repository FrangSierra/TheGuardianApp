package com.monzo.androidtest.articles.model

import android.webkit.URLUtil
import com.monzo.androidtest.api.model.ApiArticle
import com.monzo.androidtest.api.model.ApiArticleListResponse
import com.monzo.androidtest.api.model.ApiArticleResponse

class ArticleMapper {

    fun map(apiArticleListResponse: ApiArticleListResponse): List<Article> =
            apiArticleListResponse.response.results.mapNotNull { mapArticle(it) }

    fun map(apiArticleResponse: ApiArticleResponse): Article {
        return mapArticle(apiArticleResponse.response.content)
                ?: throw NullPointerException("Retrieved article is not valid")
    }

    private fun mapArticle(apiArticle: ApiArticle): Article? {
        var thumbnail: String? = null
        var headline = ""
        var body = ""

        apiArticle.fields?.let {
            val isValidUrl = URLUtil.isValidUrl(it.thumbnail)

            thumbnail = if (isValidUrl) it.thumbnail!! else null
            headline = it.headline
            body = it.body
        } ?: return null

        return Article(apiArticle.id,
                thumbnail,
                apiArticle.sectionId,
                apiArticle.sectionName,
                apiArticle.webPublicationDate,
                headline,
                apiArticle.apiUrl,
                body)
    }
}
