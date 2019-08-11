package com.monzo.androidtest.articles

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.monzo.androidtest.HeadlinesApp
import com.monzo.androidtest.R
import com.monzo.androidtest.articles.model.Article
import com.monzo.androidtest.utils.makeGone
import com.monzo.androidtest.utils.makeVisible
import com.monzo.androidtest.utils.toast
import io.reactivex.Observable
import org.jetbrains.annotations.TestOnly

class ArticleDetailActivity : AppCompatActivity(), ArticleDetailPresenter.View {
    companion object {
        private const val ARTICLE_URL = "article_url"

        fun startActivity(context: Context, articleUrl: String) {
            val intent = Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra(ARTICLE_URL, articleUrl)
            }
            context.startActivity(intent)
        }
    }

    @BindView(R.id.article_detail_toolbar)
    @JvmField
    var toolbar: Toolbar? = null

    @BindView(R.id.article_detail_image)
    @JvmField
    var articleImageView: ImageView? = null

    @BindView(R.id.article_detail_body_textview)
    @JvmField
    var articleTextView: TextView? = null

    @BindView(R.id.article_detail_title_textview)
    @JvmField
    var articleTitleTextView: TextView? = null

    @BindView(R.id.loading_progress_bar)
    @JvmField
    var progressBar: ProgressBar? = null

    @BindView(R.id.coordinator_layout)
    @JvmField
    var detailPostLayout: CoordinatorLayout? = null

    lateinit var articleUrl: String

    private lateinit var presenter: ArticleDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        ButterKnife.bind(this)
        prepareToolbar()

        articleUrl = intent.getStringExtra(ARTICLE_URL)

        presenter = HeadlinesApp.from(applicationContext).injectDetailPresenter(this)
        presenter.register(this)
    }

    private fun prepareToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { toolbar ->
            toolbar.setDisplayShowTitleEnabled(false) //TODO check how to show it only when collapse
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setDisplayShowHomeEnabled(true)
        }
    }

    override fun loadArticleAction(): Observable<String> {
        //TODO allow user to refresh
        return Observable.empty<String>().startWith(articleUrl)
    }

    override fun showArticle(article: Article) {
        articleTitleTextView?.text = article.title

        articleTextView?.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    Html.fromHtml(article.body, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(article.body)

        articleImageView?.let { Glide.with(this).load(article.thumbnail).into(it) }
    }

    override fun onError() {
        toast(getString(R.string.article_detail_load_error))
        finish()
    }

    override fun showLoadingProgressBar(show: Boolean) {
        detailPostLayout?.run { if (show) makeGone() else makeVisible() }
        progressBar?.run { if (show) makeVisible() else makeGone() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @TestOnly
    fun getPresenter() = presenter
}