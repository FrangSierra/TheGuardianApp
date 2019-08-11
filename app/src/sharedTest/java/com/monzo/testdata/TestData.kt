package com.monzo.testdata

import com.monzo.androidtest.articles.model.Article
import java.util.*

object TestData {
    val sampleArticle1 = Article(
            id = "1",
            thumbnail = "",
            sectionId = "",
            published = Date(System.currentTimeMillis()),
            sectionName = "",
            title = "title",
            url = "",
            body = ""
    )

    val sampleArticle2 = Article(
            id = "2",
            thumbnail = null,
            sectionId = "",
            published = Date(System.currentTimeMillis()),
            sectionName = "",
            title = "Another Title",
            url = "",
            body = ""
    )

    val sampleArticlesList = listOf(
            sampleArticle1,
            sampleArticle2
    )
}