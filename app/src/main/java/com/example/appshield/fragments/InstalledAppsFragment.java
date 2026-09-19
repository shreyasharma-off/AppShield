package com.example.appshield.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appshield.R;
import com.example.appshield.adapters.AppAdapter;
import com.example.appshield.models.AppInfo;
import java.util.ArrayList;
import java.util.List;

public class InstalledAppsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppAdapter adapter;
    private TextView txtEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed_apps, container, false);

        recyclerView = view.findViewById(R.id.recycler_apps);
        txtEmpty = view.findViewById(R.id.txt_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initializing with an empty list as per requirements
        List<AppInfo> appList = new ArrayList<>();
        adapter = new AppAdapter(appList, getContext());
        recyclerView.setAdapter(adapter);

        if (appList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
