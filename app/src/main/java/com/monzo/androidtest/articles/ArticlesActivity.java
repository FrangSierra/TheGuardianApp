package com.monzo.androidtest.articles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.monzo.androidtest.HeadlinesApp;
import com.monzo.androidtest.R;
import com.monzo.androidtest.articles.model.Article;
import com.monzo.androidtest.common.Event;
import com.monzo.androidtest.views.RecyclerViewEndlessScroller;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static com.monzo.androidtest.utils.AndroidExtensionsKt.makeGone;
import static com.monzo.androidtest.utils.AndroidExtensionsKt.makeVisible;

public class ArticlesActivity extends AppCompatActivity implements ArticlesPresenter.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.articles_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.articles_error_text)
    TextView errorView;

    @BindView(R.id.articles_recyclerview)
    RecyclerView recyclerView;

    private RecyclerViewEndlessScroller endlessScroller;
    private ArticlesPresenter presenter;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_list);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        adapter = new ArticleAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter = HeadlinesApp.from(getApplicationContext()).injectArticlePresenter(this);
        presenter.register(this);
    }

    @Override
    protected void onDestroy() {
        presenter.unregister();
        super.onDestroy();
    }

    @Override
    public void showArticles(List<Article> articles) {
        makeVisible(recyclerView);
        adapter.showArticles(articles);
        endlessScroller.loadFinished();
    }

    @Override
    public Observable<Article> onArticleClicked() {
        return Observable.create(emitter -> {
            adapter.setOnArticleClickListener(emitter::onNext);
            emitter.setCancellable(() -> adapter.setOnArticleClickListener(null));
        });
    }

    public Observable<Object> onScrollEnd() {
        return Observable.create(emitter -> {
            endlessScroller = new RecyclerViewEndlessScroller(emitter);
            endlessScroller.attachTo(recyclerView);
        });
    }

    @Override
    public Observable<Object> onRefreshAction() {
        return Observable.create(emitter -> {
            swipeRefreshLayout.setOnRefreshListener(() -> {
                adapter.clearAdapter();
                emitter.onNext(Event.IGNORE);
            });
            emitter.setCancellable(() -> swipeRefreshLayout.setOnRefreshListener(null));
        }).startWith(Event.IGNORE);
    }

    @Override
    public void openArticleDetail(Article article) {
        ArticleDetailActivity.Companion.startActivity(this, article.getUrl());
    }

    @Override
    public void onError() {
        makeVisible(errorView);
        makeGone(recyclerView);
    }

    @Override
    public void showRefreshing(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
