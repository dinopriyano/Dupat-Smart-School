package com.app24announce.dupat.id;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterListTerlambat extends ArrayAdapter<SiswaTerlambat> {

    private Activity context;
    private List<SiswaTerlambat> siswaTerlambat;

    public AdapterListTerlambat(Activity context, List<SiswaTerlambat> siswaTerlambat)
    {
        super(context, R.layout.card_view_daftar_telat_list,siswaTerlambat);
        this.context = context;
        this.siswaTerlambat = siswaTerlambat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.card_view_daftar_telat_list,null,true);

        TextView kelas = (TextView) listViewItem.findViewById(R.id.kelasTerlambat);
        TextView keterangan = (TextView) listViewItem.findViewById(R.id.ketTerlambat);
        TextView nama = (TextView) listViewItem.findViewById(R.id.namaTerlambat);

        SiswaTerlambat siswa = siswaTerlambat.get(position);
        kelas.setText(siswa.getKelas());
        keterangan.setText(siswa.getKeterangan());
        nama.setText(siswa.getNama());

        return listViewItem;
    }
}
