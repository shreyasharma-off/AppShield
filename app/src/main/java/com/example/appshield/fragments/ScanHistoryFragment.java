package com.example.appshield.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appshield.R;

public class ScanHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_history, container, false);
        
        TextView txtTitle = view.findViewById(R.id.txt_history_title);
        TextView txtEmpty = view.findViewById(R.id.txt_history_empty);
        
        txtTitle.setText("Scan History");
        txtEmpty.setText("No scan history available");
        
        return view;
    }
}
