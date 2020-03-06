package com.norbo.android.projects.rssolvaso.rcview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;

public class SavedHirViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvPubDate;
    TextView tvDesc;
    ImageView btnGo;

    public SavedHirViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitleSaved);
        tvPubDate = itemView.findViewById(R.id.tvPubDateSaved);
        tvDesc = itemView.findViewById(R.id.tvDescSaved);
        btnGo = itemView.findViewById(R.id.btnGoSaved);
    }
}
