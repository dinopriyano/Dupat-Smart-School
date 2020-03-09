package com.app24announce.dupat.id;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterInsideDetailJadwal2 extends RecyclerView.ViewHolder {
    TextView txtMapel;

    public AdapterInsideDetailJadwal2(@NonNull View itemView) {
        super(itemView);
        txtMapel = itemView.findViewById(R.id.txtMapel);
    }
}
