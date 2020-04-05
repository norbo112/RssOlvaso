package com.norbo.android.projects.rssolvaso.rcview;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.Zoomy;
import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;
import com.norbo.android.projects.rssolvaso.model.RssItem;

import java.util.ArrayList;
import java.util.List;

public class RssItemAdapter extends RecyclerView.Adapter<RssItemViewHolder> implements Filterable {

    private List<RssItem> rssItems;
    private List<RssItem> rssItemsFilter;
    private CimFilter cimFilter;
    private AppCompatActivity context;
    private HirSaveViewModel hirSaveViewModel;

    public RssItemAdapter(AppCompatActivity context, List<RssItem> rssItems, HirSaveViewModel pHirSaveViewModel) {
        this.context = context;
        this.rssItems = rssItems;
        this.hirSaveViewModel = pHirSaveViewModel;
        this.rssItemsFilter = rssItems;
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
        Button btnGo = holder.btnGo;
        Button btnSaveHir = holder.btnSaveHir;
        Button btnHirMegoszt = holder.btnHirShare;
        ImageView imageView = holder.imageView;

        RssItem rssItem = rssItems.get(position);
        tvTitle.setText(rssItem.getTitle());
        tvPubDate.setText(rssItem.getPubDate());
        tvDesc.setText(Html.fromHtml(rssItem.getDescription()));
        if(rssItem.getEnclosure() != null) {
            imageView.setImageBitmap(rssItem.getEnclosure());
        } else {
            imageView.setImageResource(R.drawable.ic_wallpaper_black_24dp);
        }

        Zoomy.Builder builder = new Zoomy.Builder(context)
                .target(imageView);
        builder.register();

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

        btnHirMegoszt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder
                        .from(context)
                        .setType("text/plain")
                        .setChooserTitle("Hír megosztása...")
                        .setText(link)
                        .startChooser();
            }
        });

        btnSaveHir.setOnClickListener((event) -> {
            hirSaveViewModel.insert(new HirModel(rssItem.getPubDate(), rssItem.getLink(), rssItem.getTitle(),
                    rssItem.getDescription()));
            Toast.makeText(context, "Hir mentve", Toast.LENGTH_SHORT).show();
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

    public void clear() {
        rssItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<RssItem> items) {
        rssItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(cimFilter == null) {
            cimFilter = new CimFilter();
        }
        return cimFilter;
    }

    private class CimFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                List<RssItem> filterList = new ArrayList<>();
                for(RssItem rssItem: rssItemsFilter) {
                    if(rssItem.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(rssItem);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = rssItemsFilter.size();
                results.values = rssItemsFilter;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rssItems = (List<RssItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
