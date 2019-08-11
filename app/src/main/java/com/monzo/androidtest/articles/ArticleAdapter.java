package com.monzo.androidtest.articles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.monzo.androidtest.R;
import com.monzo.androidtest.articles.model.Article;
import com.monzo.androidtest.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Article> articles = new ArrayList<>();
    private final Context context;
    private onArticleClickListener listener = null;

    ArticleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        articleViewHolder.bind(articles.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    void showArticles(List<Article> articles) {
        //TODO DiffUtil
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    void clearAdapter() {
        this.articles.clear();
        notifyDataSetChanged();
    }

    void setOnArticleClickListener(onArticleClickListener listener) {
        this.listener = listener;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.article_headline_textview)
        TextView headlineTextView;

        @BindView(R.id.article_date_textview)
        TextView dateTextView;

        @BindView(R.id.article_thumbnail_imageview)
        ImageView thumbnailImageView;

        ArticleViewHolder(View view) {
            super(view);
        }

        void bind(Article article, onArticleClickListener listener) {
            ButterKnife.bind(this, itemView);
            headlineTextView.setText(article.getTitle());
            dateTextView.setText(DateUtils.formatDate(article.getPublished()));
            itemView.setOnClickListener(view -> listener.onItemClick(article));
            Glide.with(itemView.getContext())
                    .load(article.getThumbnail())
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnailImageView);
        }
    }

    interface onArticleClickListener {
        void onItemClick(Article article);
    }
}
