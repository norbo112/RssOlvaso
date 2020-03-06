package com.norbo.android.projects.rssolvaso.rcview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;

import java.util.List;

public class RssItemAdapter extends RecyclerView.Adapter<RssItemViewHolder> {

    private List<RssItem> rssItems;
    private AppCompatActivity context;
    private HirSaveViewModel hirSaveViewModel;

    public RssItemAdapter(AppCompatActivity context, List<RssItem> rssItems, HirSaveViewModel pHirSaveViewModel) {
        this.context = context;
        this.rssItems = rssItems;
        this.hirSaveViewModel = pHirSaveViewModel;
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
        ImageView btnGo = holder.btnGo;
        ImageView btnSaveHir = holder.btnSaveHir;

        RssItem rssItem = rssItems.get(position);
        tvTitle.setText(rssItem.getTitle());
        tvPubDate.setText(rssItem.getPubDate());
        tvDesc.setText(Html.fromHtml(rssItem.getDescription()));

        final String link = rssItem.getLink();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(RssItemAdapter.class.getSimpleName(), "Választott link: "+link);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(intent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(intent);;
            }
        });

        btnSaveHir.setOnClickListener((event) -> {
            hirSaveViewModel.insert(new HirModel(rssItem.getPubDate(), rssItem.getLink(), rssItem.getTitle(),
                    rssItem.getDescription()));
            Toast.makeText(context, "Hir mentve", Toast.LENGTH_SHORT).show();
        });

        hirSaveViewModel.getHirByTitle(rssItem.getTitle()).observe(context,
                hirModel -> {
                    if(hirModel != null && hirModel.getHircim().equals(rssItem.getTitle())) {
                        btnSaveHir.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    public RssItem getRssTtem(int position) {
        return rssItems.get(position);
    }

    public List<RssItem> getRssItems() {
        return rssItems;
    }
}
