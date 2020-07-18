package com.norbo.android.projects.rssolvaso.mvvm.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.Zoomy;
import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.NewRssReaderArticleFragmentItemBinding;
import com.norbo.android.projects.rssolvaso.databinding.NewRssReaderArticleItemBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ArticleFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<Article> mValues;
    private final Fragment fragment;
    private ArticleSave articleSave;
    private ArticleView articleView;

    public interface ArticleView {
        void viewArticle(Article article);
        void shareArticle(Article article);
    }

    public interface ArticleSave {
        void saveArticle(Article article);
    }

    public ArticleFragmentRecyclerViewAdapter(Fragment fragment, List<Article> items) {
        this.mValues = items;
        this.fragment = fragment;
        this.articleSave = (ArticleSave) fragment;
        this.articleView = (ArticleView) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewRssReaderArticleFragmentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.new_rss_reader_article_fragment_item, parent, false);
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
        final NewRssReaderArticleFragmentItemBinding binding;

        public ViewHolder(NewRssReaderArticleFragmentItemBinding binding) {
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
            Zoomy.Builder builder = new Zoomy.Builder(fragment.getActivity())
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

        public void saveArticle(Article article) {
            articleSave.saveArticle(article);
        }
    }
}