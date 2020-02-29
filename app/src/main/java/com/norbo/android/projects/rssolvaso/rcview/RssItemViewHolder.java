package com.norbo.android.projects.rssolvaso.rcview;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;

public class RssItemViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvPubDate;
    TextView tvDesc;
    Button btnGo;

    public RssItemViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvPubDate = itemView.findViewById(R.id.tvPubDate);
        tvDesc = itemView.findViewById(R.id.tvDesc);
        btnGo = itemView.findViewById(R.id.btngo);
    }
}
