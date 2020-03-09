package com.app24announce.dupat.id;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDetailJadwalPage extends RecyclerView.ViewHolder{

    TextView txtHari,txtTambah;
    RecyclerView myJadwal;

    public AdapterDetailJadwalPage(@NonNull View itemView) {
        super(itemView);

        txtHari = itemView.findViewById(R.id.txtHari);
        myJadwal = itemView.findViewById(R.id.rvJdwl);
        txtTambah = itemView.findViewById(R.id.txtTambah);

    }
}
