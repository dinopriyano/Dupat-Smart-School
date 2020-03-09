package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InfoKehadiran extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

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

    String kelas;
    DatabaseReference myRef;

    private ArrayList<SetGetInfoKehadiran> arrayList;
    private FirebaseRecyclerOptions<SetGetInfoKehadiran> options;
    private FirebaseRecyclerAdapter<SetGetInfoKehadiran, AdapterInfoKehadiran> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kehadiran);

        kelas = getIntent().getStringExtra("kelas");

        final String hariIni = hri+", "+tgl+" "+bln+" "+year;

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Kehadiran Kelas "+kelas);


        myRef = FirebaseDatabase.getInstance().getReference().child("kehadiran").child(hariIni);
        myRef.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.RVInfoKehadiran);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<SetGetInfoKehadiran>();
        options = new FirebaseRecyclerOptions.Builder<SetGetInfoKehadiran>().setQuery(myRef.orderByChild("kelas").equalTo(kelas),SetGetInfoKehadiran.class).build();

        adapter = new FirebaseRecyclerAdapter<SetGetInfoKehadiran, AdapterInfoKehadiran>(options) {

            @NonNull
            @Override
            public AdapterInfoKehadiran onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterInfoKehadiran(LayoutInflater.from(InfoKehadiran.this).inflate(R.layout.row_recyclerview_info_kehadiran,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull AdapterInfoKehadiran adapterInfoKehadiran, int i, @NonNull SetGetInfoKehadiran setGetInfoKehadiran) {
                adapterInfoKehadiran.ketInfoKehadiran.setText(setGetInfoKehadiran.getKeterangan());
                adapterInfoKehadiran.namaInfoKehadiran.setText(setGetInfoKehadiran.getNama());
                adapterInfoKehadiran.nisInfoKehadiran.setText(setGetInfoKehadiran.getNis());
            }
        };

        recyclerView.setAdapter(adapter);

    }
}
