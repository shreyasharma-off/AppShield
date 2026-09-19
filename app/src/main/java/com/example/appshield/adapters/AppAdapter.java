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
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<AppInfo> appList;
    private Context context;

    public AppAdapter(List<AppInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppInfo app = appList.get(position);
        holder.txtName.setText(app.getAppName());
        holder.txtPackage.setText(app.getPackageName());
        holder.txtRiskLevel.setText(app.getRiskLevel());

        // Simple color coding for risk
        if ("High".equalsIgnoreCase(app.getRiskLevel())) {
            holder.txtRiskLevel.setBackgroundColor(0xFFFF4444);
        } else if ("Medium".equalsIgnoreCase(app.getRiskLevel())) {
            holder.txtRiskLevel.setBackgroundColor(0xFFFFBB33);
        } else {
            holder.txtRiskLevel.setBackgroundColor(0xFF99CC00);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppDetailsActivity.class);
            intent.putExtra("app_info", app);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtName, txtPackage, txtRiskLevel;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_app_icon);
            txtName = itemView.findViewById(R.id.txt_app_name);
            txtPackage = itemView.findViewById(R.id.txt_package_name);
            txtRiskLevel = itemView.findViewById(R.id.txt_risk_level);
        }
    }
}
