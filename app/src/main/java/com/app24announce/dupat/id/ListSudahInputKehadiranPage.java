package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ListSudahInputKehadiranPage extends AppCompatActivity {

    Toolbar toolbar;
    private DatabaseReference refKelasKeh;
    RecyclerView recyclerView;
    RelativeLayout rlGaAdaData;

    String month[] = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
    String hari[] = {"Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
    GregorianCalendar gc = new GregorianCalendar();
    String bln = month[gc.get(Calendar.MONTH)];
    String hri = hari[(gc.get(Calendar.DAY_OF_WEEK))-1];
    int tgl = gc.get(Calendar.DATE);
    int year = gc.get(Calendar.YEAR);
    int jam = gc.get(Calendar.HOUR);
    int mnt = gc.get(Calendar.MINUTE);
    int dt = gc.get(Calendar.SECOND);
    int pmam = gc.get(Calendar.AM_PM);

    String test;

    private ArrayList<SetGetListKelasSudahInputKehadiran> arrayList;
    private FirebaseRecyclerOptions<SetGetListKelasSudahInputKehadiran> options;
    private FirebaseRecyclerAdapter<SetGetListKelasSudahInputKehadiran, AdapterKelasSudahInputKehadiran> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sudah_input_kehadiran_page);

        final String hariIni = hri+", "+tgl+" "+bln+" "+year;

        rlGaAdaData = (RelativeLayout) findViewById(R.id.rlGaAdaData);

        refKelasKeh = FirebaseDatabase.getInstance().getReference().child("kehadiran").child("sudahinput").child(hariIni);
        refKelasKeh.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.RVSudahInputKehadiran);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlGaAdaData.setVisibility(View.GONE);


        refKelasKeh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    rlGaAdaData.setVisibility(View.GONE);
                }
                else
                {
                    rlGaAdaData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arrayList = new ArrayList<SetGetListKelasSudahInputKehadiran>();
        options = new FirebaseRecyclerOptions.Builder<SetGetListKelasSudahInputKehadiran>().setQuery(refKelasKeh,SetGetListKelasSudahInputKehadiran.class).build();

        adapter = new FirebaseRecyclerAdapter<SetGetListKelasSudahInputKehadiran, AdapterKelasSudahInputKehadiran>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final AdapterKelasSudahInputKehadiran adapterKelasSudahInputKehadiran, int i, @NonNull final SetGetListKelasSudahInputKehadiran setGetListKelasSudahInputKehadiran) {

                adapterKelasSudahInputKehadiran.kelasSudahInputKehadiran.setText(setGetListKelasSudahInputKehadiran.getKelas());

                rlGaAdaData.setVisibility(View.GONE);
                adapterKelasSudahInputKehadiran.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ListSudahInputKehadiranPage.this,InfoKehadiran.class);
                        intent.putExtra("kelas", setGetListKelasSudahInputKehadiran.getKelas());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public AdapterKelasSudahInputKehadiran onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterKelasSudahInputKehadiran(LayoutInflater.from(ListSudahInputKehadiranPage.this).inflate(R.layout.row_recyclerview_list_kelas_kehadiran,parent,false));
            }


        };


        recyclerView.setAdapter(adapter);

    }
}
