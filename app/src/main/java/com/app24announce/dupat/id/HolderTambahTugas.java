package com.app24announce.dupat.id;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderTambahTugas extends RecyclerView.ViewHolder {

    TextView myPath;

    public HolderTambahTugas(@NonNull View itemView) {
        super(itemView);

        myPath = itemView.findViewById(R.id.lokasiFile);

    }
}
