package com.app24announce.dupat.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterKehadiran extends RecyclerView.ViewHolder {

    TextView txtKeterangan,txtNama,txtNis;



    public AdapterKehadiran(@NonNull View itemView) {
        super(itemView);

        txtKeterangan = itemView.findViewById(R.id.ketGaMasuk);
        txtNama = itemView.findViewById(R.id.namaGaMasuk);
        txtNis = itemView.findViewById(R.id.nisGaMasuk);
    }
}
