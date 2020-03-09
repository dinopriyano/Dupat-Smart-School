package com.app24announce.dupat.id;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTugasGuruPage extends RecyclerView.ViewHolder {

    TextView mapelTugas,tanggalTugas,kelasTugas,deskripsiTugas;
    RelativeLayout llRowTugasGuru;
    CardView lampiranTugas;

    public AdapterTugasGuruPage(@NonNull View itemView) {
        super(itemView);

        llRowTugasGuru = itemView.findViewById(R.id.llRowTugasGuru);
        mapelTugas = itemView.findViewById(R.id.mapelTugas);
        tanggalTugas = itemView.findViewById(R.id.tanggalTugas);
        kelasTugas = itemView.findViewById(R.id.kelasTugas);
        deskripsiTugas = itemView.findViewById(R.id.descTugas);
        lampiranTugas = itemView.findViewById(R.id.lampiranTugas);

    }
}
