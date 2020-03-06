package com.norbo.android.projects.rssolvaso.rcview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.norbo.android.projects.rssolvaso.R;
import com.norbo.android.projects.rssolvaso.database.model.HirModel;
import com.norbo.android.projects.rssolvaso.database.viewmodel.HirSaveViewModel;

import java.util.List;

public class SavedHirAdapter extends RecyclerView.Adapter<SavedHirViewHolder> {
    private List<HirModel> hirModels;
    private Context context;
    private HirSaveViewModel hirSaveViewModel;

    public SavedHirAdapter(List<HirModel> hirModels, Context context, HirSaveViewModel hirSaveViewModel) {
        this.hirModels = hirModels;
        this.context = context;
        this.hirSaveViewModel = hirSaveViewModel;
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

        holder.itemView.setOnLongClickListener(v -> {
            hirSaveViewModel.delete(hirModel.getHircim());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return hirModels.size();
    }

}
