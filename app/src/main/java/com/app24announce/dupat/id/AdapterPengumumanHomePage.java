package com.app24announce.dupat.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPengumumanHomePage extends RecyclerView.Adapter<AdapterPengumumanHomePage.MyViewHolder>{

    Context ctx;
    List<SetGetListInfo> list;

    public AdapterPengumumanHomePage(Context ctx, List<SetGetListInfo> list)
    {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterPengumumanHomePage.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx.getApplicationContext()).inflate(R.layout.row_recyclerview_pengumuman_home,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPengumumanHomePage.MyViewHolder holder, int position) {
        SetGetListInfo setGetListInfo = list.get(position);
        holder.txtInfo.setText(setGetListInfo.getInfo());
        String[] wak = setGetListInfo.getTime().split(" ");
        String date = wak[0];
        String jam = wak[1];
        holder.txtWkt.setText(date+" at "+jam);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtJdl,txtWkt,txtInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtJdl = itemView.findViewById(R.id.txtJdl);
            txtWkt = itemView.findViewById(R.id.txtWkt);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}
