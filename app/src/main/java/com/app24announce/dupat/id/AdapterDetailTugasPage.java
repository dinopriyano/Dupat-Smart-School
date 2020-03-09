package com.app24announce.dupat.id;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterDetailTugasPage extends RecyclerView.ViewHolder {

    TextView namaFile,ukuranFile;
    ImageButton ivDownloadFile;

    public AdapterDetailTugasPage(@NonNull View itemView) {
        super(itemView);

        namaFile = itemView.findViewById(R.id.namaFile);
        ukuranFile = itemView.findViewById(R.id.ukuranFile);
        ivDownloadFile = itemView.findViewById(R.id.ivDownloadFile);

    }
}
