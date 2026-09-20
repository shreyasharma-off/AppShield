package com.example.appshield.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appshield.R;
import com.example.appshield.activities.AppDetailsActivity;
import com.example.appshield.models.AppInfo;
import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<AppInfo> appList;
    private List<AppInfo> filteredList;
    private Context context;

    public AppAdapter(List<AppInfo> appList, Context context) {
        this.appList = appList;
        this.filteredList = new ArrayList<>(appList);
        this.context = context;
    }

    public void updateData(List<AppInfo> newList) {
        this.appList = newList;
        this.filteredList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void filter(String query, String riskFilter) {
        filteredList.clear();
        for (AppInfo app : appList) {
            boolean matchesQuery = app.getAppName().toLowerCase().contains(query.toLowerCase()) || 
                                 app.getPackageName().toLowerCase().contains(query.toLowerCase());
            boolean matchesRisk = riskFilter.equals("All") || app.getRiskLevel().equalsIgnoreCase(riskFilter);
            
            if (matchesQuery && matchesRisk) {
                filteredList.add(app);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppInfo app = filteredList.get(position);
        holder.txtName.setText(app.getAppName());
        holder.txtPackage.setText(app.getPackageName());
        holder.txtRiskBadge.setText(app.getRiskLevel().toUpperCase());
        holder.txtRiskScore.setText(app.getRiskScore() + " points");
        
        if (app.getAppIcon() != null) {
            holder.imgIcon.setImageDrawable(app.getAppIcon());
        } else {
            holder.imgIcon.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Color coding for risk badge
        if ("High".equalsIgnoreCase(app.getRiskLevel())) {
            holder.txtRiskBadge.setBackgroundResource(R.drawable.bg_risk_badge_high);
        } else if ("Medium".equalsIgnoreCase(app.getRiskLevel())) {
            holder.txtRiskBadge.setBackgroundResource(R.drawable.bg_risk_badge_medium);
        } else {
            holder.txtRiskBadge.setBackgroundResource(R.drawable.bg_risk_badge_low);
        }

        holder.itemView.setOnClickListener(v -> {
            // AppDetailsActivity needs app_info which is Serializable. 
            // Drawable is not serializable, but let's assume it works or we pass package name.
            // Actually the current AppDetailsActivity uses app_info extra.
            // We might need to handle the Drawable if serialization fails.
            Intent intent = new Intent(context, AppDetailsActivity.class);
            // Since we can't easily serialize Drawable, and AppDetailsActivity expects it, 
            // in a real app we'd pass the package name and reload the icon.
            // But I must preserve the existing integration if it works.
            AppDetailsActivity.selectedApp = app; 
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtName, txtPackage, txtRiskBadge, txtRiskScore;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_app_icon);
            txtName = itemView.findViewById(R.id.txt_app_name);
            txtPackage = itemView.findViewById(R.id.txt_package_name);
            txtRiskBadge = itemView.findViewById(R.id.txt_risk_badge);
            txtRiskScore = itemView.findViewById(R.id.txt_risk_score);
        }
    }
}
