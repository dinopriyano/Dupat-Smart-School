package com.app24announce.dupat.id;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterInsideDetailJadwal extends RecyclerView.Adapter<AdapterInsideDetailJadwal.MyViewHolder>{

    private List<SetGetInsideDetailJadwal> list;
    private Context ctx;
    DetailJadwalPage detailJadwalPage = new DetailJadwalPage();
    String ketuakelas,gurupiket,posisi,kelassis,kelas;

    public AdapterInsideDetailJadwal(List<SetGetInsideDetailJadwal> mapelList, Context ctx, String ketuakelas, String posisi, String gurupiket, String kelas, String kel) {
        this.list = mapelList;
        this.ctx = ctx;
        this.ketuakelas = ketuakelas;
        this.gurupiket = gurupiket;
        this.posisi = posisi;
        this.kelassis = kelas;
        this.kelas = kel;
        
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx.getApplicationContext()).inflate(R.layout.row_recyclerview_inside_detail_jadwal_page,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SetGetInsideDetailJadwal setGetInsideDetailJadwal = list.get(position);

        RelativeLayout rv = (RelativeLayout) ((Activity)ctx).findViewById(R.id.bgGelap);
        LinearLayout ll = (LinearLayout) ((Activity)ctx).findViewById(R.id.isiPopup);
        holder.txtWaktu.setText(setGetInsideDetailJadwal.getWaktuawal()+" - "+setGetInsideDetailJadwal.getWaktuakhir());
        holder.txtMapel.setText(setGetInsideDetailJadwal.getMapel());
//        holder.cvClickMapel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rv.setVisibility(View.VISIBLE);
//                ll.setAlpha(1);
//                ll.setVisibility(View.VISIBLE);
//            }
//        });

        if(posisi.equals("guru"))
        {
            if(gurupiket.equals("1"))
            {
                holder.btnDelete.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.btnDelete.setVisibility(View.GONE);
            }
        }
        else
        {
            if(ketuakelas.equals("1"))
            {
                if(kelassis.equals(kelas))
                {
                    holder.btnDelete.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.btnDelete.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.btnDelete.setVisibility(View.GONE);
            }
        }
        
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = setGetInsideDetailJadwal.getId();
                String hari = setGetInsideDetailJadwal.getHari();

                new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Ingin menghapus mapel?")
                        .setConfirmText("Hapus")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                FirebaseDatabase.getInstance().getReference().child("jadwal").child(kelas).child("jadwal").child(hari).child("mapel").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        sweetAlertDialog.setTitleText("Berhasil!")
                                                .setContentText("Berhasil menghapus mapel.")
                                                .setConfirmText("Oke")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                });

                            }
                        })
                        .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtMapel,txtWaktu;
        LinearLayout cvMapel;
        CardView cvClickMapel;
        FloatingActionButton btnDelete;
        RelativeLayout lv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWaktu = (TextView) itemView.findViewById(R.id.txtWaktu);
            cvClickMapel = (CardView)itemView.findViewById(R.id.cvClickMapel);
            txtMapel = (TextView) itemView.findViewById(R.id.txtMapel);
            cvMapel = (LinearLayout) itemView.findViewById(R.id.cvMapel);
            btnDelete = (FloatingActionButton) itemView.findViewById(R.id.btnDelete);

        }
    }
}
