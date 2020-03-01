package com.norbo.android.projects.rssolvaso.rcview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.model.RssItem;

import java.util.List;

public class RssItemAdapter extends RecyclerView.Adapter<RssItemViewHolder> {

    private List<RssItem> rssItems;
    private Context context;

    public RssItemAdapter(Context context, List<RssItem> rssItems) {
        this.context = context;
        this.rssItems = rssItems;
    }

    @NonNull
    @Override
    public RssItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rssitemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rssitem_layout, parent, false);
        return new RssItemViewHolder(rssitemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RssItemViewHolder holder, int position) {
        TextView tvTitle = holder.tvTitle;
        TextView tvPubDate = holder.tvPubDate;
        TextView tvDesc = holder.tvDesc;
        FloatingActionButton btnGo = holder.btnGo;

        RssItem rssItem = rssItems.get(position);
        tvTitle.setText(rssItem.getTitle());
        tvPubDate.setText(rssItem.getPubDate());
        //tvDesc.setText(rssItem.getDescription());
        tvDesc.setText(Html.fromHtml(rssItem.getDescription()));

        final String link = rssItem.getLink();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(RssItemAdapter.class.getSimpleName(), "Választott link: "+link);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                if(intent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(intent);;
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }
}
