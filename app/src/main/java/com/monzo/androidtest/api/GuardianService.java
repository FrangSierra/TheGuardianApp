package com.monzo.androidtest.api;

import com.monzo.androidtest.api.model.ApiArticleListResponse;
import com.monzo.androidtest.api.model.ApiArticleResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GuardianService {
    @GET("search?show-fields=headline,thumbnail")
    Single<ApiArticleListResponse> searchArticles(@Query("page") Long pageNumber);

    @GET
    Single<ApiArticleResponse> getArticle(@Url String articleUrl, @Query("show-fields") String fields);
}
