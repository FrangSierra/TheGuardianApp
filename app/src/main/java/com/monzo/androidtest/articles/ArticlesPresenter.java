package com.monzo.androidtest.articles;

import android.util.Log;

import com.monzo.androidtest.articles.domain.ArticlesController;
import com.monzo.androidtest.articles.model.Article;
import com.monzo.androidtest.common.BasePresenter;
import com.monzo.androidtest.common.BasePresenterView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

class ArticlesPresenter extends BasePresenter<ArticlesPresenter.View> {
    private final Scheduler uiScheduler;
    private final ArticlesController articlesController;

    ArticlesPresenter(Scheduler uiScheduler, ArticlesController articlesController) {
        this.uiScheduler = uiScheduler;
        this.articlesController = articlesController;
    }

    @Override
    public void register(@NotNull View view) {
        super.register(view);

        addToUnsubscribe(view.onRefreshAction()
                .doOnNext(ignored -> view.showRefreshing(true))
                .switchMapSingle(ignored -> searchArticles(true))
                .observeOn(uiScheduler)
                .subscribe((articles -> {
                    view.showRefreshing(false);
                    if (articles.isEmpty()) view.onError();
                    else view.showArticles(articles);
                })));


        addToUnsubscribe(view.onScrollEnd()
                .switchMapSingle(ignored -> searchArticles(false))
                .observeOn(uiScheduler)
                .subscribe((view::showArticles)));

        addToUnsubscribe(view.onArticleClicked().subscribe(view::openArticleDetail));
    }

    private Single<List<Article>> searchArticles(boolean reload) {
        return articlesController.searchArticles(reload)
                .onErrorResumeNext(error -> {
                    Log.e("ArticlesPresenter", "Error when loading articles", error);
                    return Single.just(Collections.emptyList());
                });
    }

    interface View extends BasePresenterView {
        void showRefreshing(boolean isRefreshing);

        void showArticles(List<Article> articles);

        void onError(); //TODO customize based on error

        Observable<Article> onArticleClicked();

        Observable<Object> onScrollEnd();

        Observable<Object> onRefreshAction();

        void openArticleDetail(Article article);
    }
}