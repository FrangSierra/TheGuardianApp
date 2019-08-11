package com.monzo.androidtest.articles.model

import java.util.*

data class Article(
        val id: String,
        val thumbnail: String?,
        val sectionId: String,
        val sectionName: String,
        val published: Date,
        val title: String,
        val url: String,
        val body: String?
) {
    //TODO Add more api nullable checks
    fun hasExtraFields() = thumbnail != null
}