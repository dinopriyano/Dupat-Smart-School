package com.app24announce.dupat.id;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterJadwalPage extends RecyclerView.ViewHolder{

    TextView txtKelas;
    CardView rowJadwal;
    View viewWarna;

    public AdapterJadwalPage(@NonNull View itemView) {
        super(itemView);

        viewWarna = itemView.findViewById(R.id.viewWarna);
        txtKelas = itemView.findViewById(R.id.txtKelas);
        rowJadwal = itemView.findViewById(R.id.rowJadwalPage);
    }
}
