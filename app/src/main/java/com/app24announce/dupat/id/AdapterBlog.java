package com.app24announce.dupat.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBlog extends RecyclerView.Adapter<AdapterBlog.ViewHolder> {

    List<SetGetItemBlog> list;
    Context ctx;

    public AdapterBlog(List<SetGetItemBlog> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public AdapterBlog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview_blog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBlog.ViewHolder holder, int position) {

        SetGetItemBlog model = list.get(position);

        holder.tittleBlog.setText(model.getTitleBlog());
        holder.dateBlog.setText(model.getDateUpload());
        holder.nameUserBlog.setText(model.getNameUser());

        Picasso.get().load(model.getPhotoUser()).into(holder.ppUserBlog);
        Picasso.get().load(model.getPhotoBlog()).into(holder.ppBlog);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ppUserBlog,ppBlog;
        TextView nameUserBlog,dateBlog,tittleBlog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ppUserBlog = itemView.findViewById(R.id.ppUserBlog);
            ppBlog = itemView.findViewById(R.id.ppBlog);
            nameUserBlog = itemView.findViewById(R.id.nameUserBlog);
            dateBlog = itemView.findViewById(R.id.dateBlog);
            tittleBlog = itemView.findViewById(R.id.titleBlog);

        }
    }
}
