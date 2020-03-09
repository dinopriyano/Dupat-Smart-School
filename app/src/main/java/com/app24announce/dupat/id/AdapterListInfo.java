package com.app24announce.dupat.id;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterListInfo extends RecyclerView.Adapter<AdapterListInfo.MyViewHolder> {
    private Context context;
    private List<SetGetListInfo> info;

    public AdapterListInfo(Context ctx, List<SetGetListInfo> info)
    {
        this.context = ctx;
        this.info = info;
    }


    @NonNull
    @Override
    public AdapterListInfo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_list_tambah_info,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListInfo.MyViewHolder holder, int position) {
        SetGetListInfo setGetListInfo = info.get(position);
        holder.txtInfoPP.setText(setGetListInfo.getInfo());
        holder.txtTimePP.setText(setGetListInfo.getTime());
        //holder.constructor.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtInfoPP,txtTimePP;
        RelativeLayout constructor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constructor = itemView.findViewById(R.id.constructor);
            txtTimePP = itemView.findViewById(R.id.txtTimePP);
            txtInfoPP = itemView.findViewById(R.id.txtInfoPP);
        }
    }
}
