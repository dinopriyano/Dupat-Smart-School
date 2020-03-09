package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JadwalPage extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView myJadwal;

    private ArrayList<SetGetJadwalPage> arrayList;
    private FirebaseRecyclerOptions<SetGetJadwalPage> options;
    private FirebaseRecyclerAdapter<SetGetJadwalPage, AdapterJadwalPage> adapter;
    String posisi,gurupiket,ketuakelas,kelassis;

    DatabaseReference myRefJadwal;
    FirebaseAuth myAuth;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_page);

        posisi = getIntent().getStringExtra("posSis");
        gurupiket = getIntent().getStringExtra("guruPik");
        ketuakelas = getIntent().getStringExtra("ketuaKel");
        kelassis = getIntent().getStringExtra("kelSis");

        myJadwal = (RecyclerView) findViewById(R.id.myJadwal);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        myJadwal.setHasFixedSize(true);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        myJadwal.setLayoutManager(linearLayoutManager);

        myAuth = FirebaseAuth.getInstance();
        myRefJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal");
        myRefJadwal.keepSynced(true);

        arrayList = new ArrayList<SetGetJadwalPage>();
        options = new FirebaseRecyclerOptions.Builder<SetGetJadwalPage>().setQuery(myRefJadwal.orderByChild("kelas"),SetGetJadwalPage.class).build();

        adapter = new FirebaseRecyclerAdapter<SetGetJadwalPage, AdapterJadwalPage>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterJadwalPage adapterJadwalPage, int i, @NonNull SetGetJadwalPage setGetJadwalPage) {
                adapterJadwalPage.txtKelas.setText(setGetJadwalPage.getKelas());

                String[] regex ={"RPL","TBS","TBG","UPW","PH"};
                String[] color = {"#03a9fc","#f948c3","#009444","#ffe315","#ed1c24"};
                for(int x = 0;x<regex.length;x++)
                {
                    int same = 0;
                    Pattern pattern = Pattern.compile(regex[x]);
                    Matcher matcher = pattern.matcher(setGetJadwalPage.getKelas());
                    while(matcher.find())
                    {
                        same++;
                    }
                    if(same!=0)
                    {
                        adapterJadwalPage.viewWarna.setBackgroundColor(Color.parseColor(color[x]));
                    }
                }

                adapterJadwalPage.rowJadwal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(JadwalPage.this,DetailJadwalPage.class);
                        intent.putExtra("kelas",setGetJadwalPage.getKelas());
                        intent.putExtra("posisi",posisi);
                        intent.putExtra("kelassis",kelassis);
                        intent.putExtra("gurupiket",gurupiket);
                        intent.putExtra("ketuakelas",ketuakelas);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public AdapterJadwalPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterJadwalPage(LayoutInflater.from(JadwalPage.this).inflate(R.layout.row_recyclerview_jadwal_page,parent,false));
            }
        };

        myJadwal.setAdapter(adapter);

    }


}
