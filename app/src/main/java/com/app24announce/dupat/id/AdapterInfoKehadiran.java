package com.app24announce.dupat.id;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterInfoKehadiran extends RecyclerView.ViewHolder {

    TextView ketInfoKehadiran,namaInfoKehadiran,nisInfoKehadiran;

    public AdapterInfoKehadiran(@NonNull View itemView) {
        super(itemView);

        ketInfoKehadiran = itemView.findViewById(R.id.ketInfoKehadiran);
        namaInfoKehadiran = itemView.findViewById(R.id.namaInfoKehadiran);
        nisInfoKehadiran = itemView.findViewById(R.id.nisInfoKehadiran);
    }
}
