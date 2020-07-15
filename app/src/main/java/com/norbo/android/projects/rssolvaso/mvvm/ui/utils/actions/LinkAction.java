package com.norbo.android.projects.rssolvaso.mvvm.ui.utils.actions;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.databinding.LinkAddDialogBinding;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Link;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

@ActivityContext
public class LinkAction {
    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;

    public interface Callback {
        void action(int mode, Link link);
    }

    private Context context;

    @Inject
    public LinkAction(@ActivityContext Context context) {
        this.context = context;
    }

    public void showDialog(int mode, Link link, Callback callback) {
        LinkAddDialogBinding binding = LinkAddDialogBinding.inflate(LayoutInflater.from(context),
                null, false);
        if(link == null) link = new Link();

        binding.setCsatorna(link);
        new AlertDialog.Builder(context)
                .setMessage(getMessage(mode))
                .setView(binding.getRoot())
                .setNegativeButton("mégse", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("ok", (dialog, which) -> {
                    Link csatorna = binding.getCsatorna();
                    callback.action(mode, csatorna);
                })
                .show();
    }

    private String getMessage(int mode) {
        switch (mode) {
            case ACTION_ADD : return context.getResources().getString(R.string.link_title_add);
            case ACTION_EDIT: return context.getResources().getString(R.string.link_title_set);
            default: return null;
        }
    }
}
