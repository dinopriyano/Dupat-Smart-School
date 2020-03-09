package com.app24announce.dupat.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTambahTugas extends RecyclerView.Adapter<HolderTambahTugas> {

    Context context;
    ArrayList<SetGetAttachmentTambahTugas> models;

    public AdapterTambahTugas(Context context, ArrayList<SetGetAttachmentTambahTugas> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public HolderTambahTugas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_recyclerview_item,null);

        return new HolderTambahTugas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTambahTugas holder, int position) {
        holder.myPath.setText(models.get(position).getPath());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}