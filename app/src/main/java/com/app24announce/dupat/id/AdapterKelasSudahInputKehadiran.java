package com.app24announce.dupat.id;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterKelasSudahInputKehadiran extends RecyclerView.ViewHolder{

    TextView kelasSudahInputKehadiran;


    public AdapterKelasSudahInputKehadiran(@NonNull View itemView) {
        super(itemView);

        kelasSudahInputKehadiran = itemView.findViewById(R.id.kelasSudahInputKehadiran);
    }
}
