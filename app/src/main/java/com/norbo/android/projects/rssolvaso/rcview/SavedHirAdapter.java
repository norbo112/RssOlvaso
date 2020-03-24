package com.norbo.android.projects.rssolvaso.rcview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.acutils.LongClickTorlesre;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedHirAdapter extends RecyclerView.Adapter<SavedHirViewHolder> implements Filterable {
    private List<HirModel> hirModels;
    private List<HirModel> hirModelsFiltered;
    private Context context;
    private HirSaveViewModel hirSaveViewModel;
    private ValueFilter filter;
    private LongClickTorlesre longClickTorlesre;

    public SavedHirAdapter(List<HirModel> hirModels, Context context, HirSaveViewModel hirSaveViewModel) {
        this.hirModels = hirModels;
        this.context = context;
        this.hirSaveViewModel = hirSaveViewModel;
        this.hirModelsFiltered = hirModels;
    }

    @NonNull
    @Override
    public SavedHirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View hirsavedview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rssitem_layout_saved,parent, false);
        return new SavedHirViewHolder(hirsavedview);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHirViewHolder holder, int position) {
        TextView tvTitle = holder.tvTitle;
        TextView tvPubDate = holder.tvPubDate;
        TextView tvDesc = holder.tvDesc;
        ImageView btnGo = holder.btnGo;
        ImageView btnShare = holder.btnShare;

        HirModel hirModel = hirModels.get(position);
        tvTitle.setText(hirModel.getHircim());
        tvPubDate.setText(hirModel.getPubdate());
        tvDesc.setText(Html.fromHtml(hirModel.getHirdesc()));

        final String link = hirModel.getLink();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(intent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(intent);;
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            longClickTorlesre.longClickTorol(hirModel);
//            hirSaveViewModel.delete(hirModel.getHircim());
//            torol(hirModel);
            return false;
        });
    }

    public void setLongClickTorlesre(LongClickTorlesre longClickTorlesre) {
        this.longClickTorlesre = longClickTorlesre;
    }

    public HirModel getModelByPos(int pos) { return hirModels.get(pos); }

    public HirSaveViewModel getHirSaveViewModel() {
        return hirSaveViewModel;
    }

    @Override
    public int getItemCount() {
        return hirModels.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new ValueFilter();
        }
        return filter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                List<HirModel> filterList = new ArrayList<>();
                for(HirModel model: hirModelsFiltered) {
                    if(model.getHircim().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(model);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = hirModelsFiltered.size();
                results.values = hirModelsFiltered;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            hirModels = (List<HirModel>) results.values;
            notifyDataSetChanged();
        }
    }

    public static void torol(Context context, HirModel hirModel, HirSaveViewModel hirSaveViewModel) {
        new AlertDialog.Builder(context)
                .setTitle("Törlés")
                .setMessage(hirModel.getPubdate()+" hírt biztosan törlöd?!")
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hirSaveViewModel.delete(hirModel.getHircim());
                    }
                })
                .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nem törölted
                    }
                }).create().show();
    }
}
