package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.Zoomy;
import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.NewRssReaderArticleSavedItemBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleSavedRecyclerViewAdapter extends RecyclerView.Adapter<ArticleSavedRecyclerViewAdapter.ViewHolder> {

    private final List<Article> mValues;
    private final Context context;
    private ArticleView articleView;
    private ArticleDelete articleDelete;

    public ArticleSavedRecyclerViewAdapter(Context context, List<Article> items) {
        this.mValues = items;
        this.context = context;
        this.articleView = (ArticleView) context;
        this.articleDelete = (ArticleDelete) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewRssReaderArticleSavedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.new_rss_reader_article_saved_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final NewRssReaderArticleSavedItemBinding binding;

        public ViewHolder(NewRssReaderArticleSavedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Article currentArticle) {
            binding.setArticle(currentArticle);
            init(currentArticle);
            binding.setAction(new Actioner());
            binding.executePendingBindings();
        }

        private void init(Article article) {
            Zoomy.Builder builder = new Zoomy.Builder((Activity) context)
                    .target(binding.imageView);
            builder.register();
            if (article.getImageUrl() != null && article.getImageUrl().length() > 0) {
                Picasso.get()
                        .load(article.getImageUrl())
                        .fit()
                        .into(binding.imageView);
            } else {
                binding.imageView.setImageResource(android.R.drawable.dialog_holo_light_frame);
            }

            //TODO átalakítandó, kell egy Article osztály amit a megjelenítéshez használok csak
            binding.articleDescription.setText(Html.fromHtml(article.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        }
    }

    public class Actioner {
        public void viewArticle(Article article) {
            articleView.viewArticle(article);
        }

        public void shareArticle(Article article) {
            articleView.shareArticle(article);
        }

        public void deleteArticle(Article article) {
            articleDelete.delete(article);
        }
    }
}