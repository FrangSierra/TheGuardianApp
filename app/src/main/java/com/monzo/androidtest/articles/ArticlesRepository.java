package com.monzo.androidtest.articles;


import com.monzo.androidtest.api.GuardianService;
import com.monzo.androidtest.articles.model.Article;
import com.monzo.androidtest.articles.model.ArticleMapper;

import java.util.List;

import io.reactivex.Single;


public class ArticlesRepository {
    private final GuardianService guardianService;
    private final ArticleMapper articleMapper;

    public ArticlesRepository(GuardianService guardianService, ArticleMapper articleMapper) {
        this.guardianService = guardianService;
        this.articleMapper = articleMapper;
    }

    public Single<List<Article>> latestArticles(Long page) {
        return guardianService.searchArticles(page).map(articleMapper::map);
    }

    public Single<Article> getArticle(String articleUrl) {
        return guardianService.getArticle(articleUrl, "main,body,headline,thumbnail").map(articleMapper::map);
    }
}
