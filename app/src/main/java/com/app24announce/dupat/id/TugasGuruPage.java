package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TugasGuruPage extends AppCompatActivity {

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
    String hariIni = hri+", "+tgl+" "+bln+" "+year;

    RelativeLayout rlGaAdaData,bgGelapTugas;
    CardView cvHapusTugas;
    Button btnHapusTugas,btnTutupTugas;
    Animation fromsmall;

    String posisi,gurupiket;

    String TAG = "TugasGuruPage";

    RecyclerView RVTugasGuru;
    Query queryTugasGuru;

    Toolbar toolbar;

    RelativeLayout tugasGuruPage;

    private ArrayList<SetGetTugasGuruPage> arrayList;
    private FirebaseRecyclerOptions<SetGetTugasGuruPage> options;
    private FirebaseRecyclerAdapter<SetGetTugasGuruPage, AdapterTugasGuruPage> adapter;

    DatabaseReference myRefHome,refTugasGuru;
    FirebaseAuth myAuth;

    FloatingActionMenu btnTambahTugas;

    String potSis,posSis,namaSis,kelSis,nisSis,nuptkGur,emailSis,passSis,fotoSis,guruPik,ketuaKel;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas_guru_page);

        bgGelapTugas = (RelativeLayout) findViewById(R.id.bgGelapTugas);
        cvHapusTugas = (CardView) findViewById(R.id.cvHapusTugas);
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall);
        bgGelapTugas.setAlpha(0);
        bgGelapTugas.setVisibility(View.GONE);
        cvHapusTugas.setAlpha(0);
        cvHapusTugas.setVisibility(View.GONE);
        btnHapusTugas = (Button) findViewById(R.id.btnHapusTugas);
        btnTutupTugas = (Button) findViewById(R.id.btnTutupTugas);

        posisi = getIntent().getStringExtra("posisiNya");
        gurupiket = getIntent().getStringExtra("guruPiketNya");

        rlGaAdaData = (RelativeLayout) findViewById(R.id.rlGaAdaData);
        tugasGuruPage = (RelativeLayout) findViewById(R.id.tugasGuruPage);

        RVTugasGuru = (RecyclerView) findViewById(R.id.RVTugasGuru);
        RVTugasGuru.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        RVTugasGuru.setLayoutManager(linearLayoutManager);

        btnTambahTugas = (FloatingActionMenu) findViewById(R.id.btnTambahTugas);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        refTugasGuru = FirebaseDatabase.getInstance().getReference().child("tugas");
        refTugasGuru.keepSynced(true);
        queryTugasGuru = FirebaseDatabase.getInstance().getReference().child("tugas");

        arrayList = new ArrayList<SetGetTugasGuruPage>();
        options = new FirebaseRecyclerOptions.Builder<SetGetTugasGuruPage>().setQuery(refTugasGuru.orderByChild("timestamp"),SetGetTugasGuruPage.class).build();

        myRefHome = FirebaseDatabase.getInstance().getReference("user").child(myAuth.getUid());
        myRefHome.keepSynced(true);

        rlGaAdaData.setVisibility(View.GONE);
        String suksesTambahTugas = null;

        loadRecyclerView();

        btnTambahTugas.setEnabled(false);
        btnTambahTugas.setVisibility(View.GONE);


        if(getIntent().getExtras() != null)
        {
            boolean up = getIntent().getExtras().getBoolean("update");
            if(up)
            {
                String sukses = getIntent().getExtras().getString("suksesTambahTugas");
                if(sukses.equals("1"))
                {
                    Snackbar.make(findViewById(R.id.tugasGuruPage),"berhasil menambahkan tugas", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                }
            }

        }


        myRefHome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nama = dataSnapshot.child("nama").getValue().toString();
                String kelas = dataSnapshot.child("kelas").getValue().toString();
                String nis = dataSnapshot.child("nis").getValue().toString();
                String pos = dataSnapshot.child("posisi").getValue().toString();
                String ketu = dataSnapshot.child("ketuakelas").getValue().toString();
                String gurupiket = dataSnapshot.child("gurupiket").getValue().toString();
                String pp = dataSnapshot.child("fotoprofil").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String pass = dataSnapshot.child("password").getValue().toString();
                nuptkGur = dataSnapshot.child("nuptk").getValue().toString();

                guruPik = gurupiket;
                ketuaKel = ketu;
                fotoSis = pp;
                emailSis = email;
                passSis = pass;
                posSis = pos;
                kelSis = kelas;
                nisSis = nis;
                namaSis = nama;
                potSis = pp;


                refData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










        btnTambahTugas.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TugasGuruPage.this, TambahTugasPage.class);
                intent.putExtra("nama",namaSis);
                intent.putExtra("pos",posisi);
                intent.putExtra("gurpik",gurupiket);
                startActivity(intent);
            }
        });


    }

    public void loadRecyclerView()
    {

        RVTugasGuru.removeAllViewsInLayout();

        adapter = new FirebaseRecyclerAdapter<SetGetTugasGuruPage, AdapterTugasGuruPage>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdapterTugasGuruPage adapterTugasGuruPage, int i, @NonNull final SetGetTugasGuruPage setGetTugasGuruPage) {
                adapterTugasGuruPage.tanggalTugas.setText(setGetTugasGuruPage.getTanggal());
                adapterTugasGuruPage.mapelTugas.setText(setGetTugasGuruPage.getMapel());
                adapterTugasGuruPage.kelasTugas.setText(setGetTugasGuruPage.getKelastujuan());
                adapterTugasGuruPage.deskripsiTugas.setText(setGetTugasGuruPage.getKeterangan());

                myRefHome.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String nama = dataSnapshot.child("nama").getValue().toString();
                        String kelas = dataSnapshot.child("kelas").getValue().toString();
                        String nis = dataSnapshot.child("nis").getValue().toString();
                        String pos = dataSnapshot.child("posisi").getValue().toString();
                        String ketu = dataSnapshot.child("ketuakelas").getValue().toString();
                        String gurupiket = dataSnapshot.child("gurupiket").getValue().toString();
                        String pp = dataSnapshot.child("fotoprofil").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String pass = dataSnapshot.child("password").getValue().toString();
                        nuptkGur = dataSnapshot.child("nuptk").getValue().toString();

                        guruPik = gurupiket;
                        ketuaKel = ketu;
                        fotoSis = pp;
                        emailSis = email;
                        passSis = pass;
                        posSis = pos;
                        kelSis = kelas;
                        nisSis = nis;
                        namaSis = nama;
                        potSis = pp;

                        //Toast.makeText(TugasGuruPage.this, myAuth.getUid().toString(), Toast.LENGTH_SHORT).show();

                        if(posSis.equals("guru"))
                        {
                            if(guruPik.equals("0"))
                            {
                                if(!setGetTugasGuruPage.getUid().equals(myAuth.getUid().toString()))
                                {
                                    adapterTugasGuruPage.llRowTugasGuru.setVisibility(View.GONE);
                                }
                                else
                                {

                                    if(setGetTugasGuruPage.getAdalampiran().equals("0"))
                                    {
                                        adapterTugasGuruPage.lampiranTugas.setVisibility(View.GONE);
                                    }
                                }

                                adapterTugasGuruPage.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {

                                        bgGelapTugas.setVisibility(View.VISIBLE);
                                        bgGelapTugas.setAlpha(1);
                                        cvHapusTugas.setVisibility(View.VISIBLE);
                                        cvHapusTugas.setAlpha(1);
                                        cvHapusTugas.startAnimation(fromsmall);

                                        btnTutupTugas.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                bgGelapTugas.setAlpha(0);
                                                bgGelapTugas.setVisibility(View.GONE);
                                                cvHapusTugas.setAlpha(0);
                                                cvHapusTugas.setVisibility(View.GONE);
                                            }
                                        });

                                        btnHapusTugas.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                FirebaseDatabase.getInstance().getReference().child(setGetTugasGuruPage.getKelastujuan()).child("tugas").child(setGetTugasGuruPage.getTanggal()).child(setGetTugasGuruPage.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference().child("tugas").child(setGetTugasGuruPage.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseStorage.getInstance().getReference().child("tugas").child(setGetTugasGuruPage.getKelastujuan()).child(setGetTugasGuruPage.getTanggal()).child(setGetTugasGuruPage.getMapel()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        bgGelapTugas.setAlpha(0);
                                                                        bgGelapTugas.setVisibility(View.GONE);
                                                                        cvHapusTugas.setAlpha(0);
                                                                        cvHapusTugas.setVisibility(View.GONE);
                                                                        Snackbar.make(findViewById(R.id.tugasGuruPage),"berhasil menghapus tugas", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        return true;


                                    }
                                });


                            }
                            else
                            {
                                rlGaAdaData.setVisibility(View.GONE);
                                if(setGetTugasGuruPage.getAdalampiran().equals("0"))
                                {
                                    adapterTugasGuruPage.lampiranTugas.setVisibility(View.GONE);
                                }
                            }

                            refData();
                        }

                        else
                        {
                            if(!setGetTugasGuruPage.getKelastujuan().equals(kelSis))
                            {
                                adapterTugasGuruPage.llRowTugasGuru.setVisibility(View.GONE);
                            }
                            else
                            {
                                rlGaAdaData.setVisibility(View.GONE);
                                if(setGetTugasGuruPage.getAdalampiran().equals("0"))
                                {
                                    adapterTugasGuruPage.lampiranTugas.setVisibility(View.GONE);
                                }
                            }

                            refData();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                adapterTugasGuruPage.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(TugasGuruPage.this,DetailTugasPage.class);
                        intent.putExtra("adalampiran",setGetTugasGuruPage.getAdalampiran());
                        intent.putExtra("id",setGetTugasGuruPage.getId());
                        intent.putExtra("kelastujuan",setGetTugasGuruPage.getKelastujuan());
                        intent.putExtra("keterangan",setGetTugasGuruPage.getKeterangan());
                        intent.putExtra("mapel",setGetTugasGuruPage.getMapel());
                        intent.putExtra("namaguru",setGetTugasGuruPage.getNamaguru());
                        intent.putExtra("tanggal",setGetTugasGuruPage.getTanggal());
                        intent.putExtra("uid",setGetTugasGuruPage.getUid());
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public AdapterTugasGuruPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterTugasGuruPage(LayoutInflater.from(TugasGuruPage.this).inflate(R.layout.row_recyclerview_tugas_guru_page,parent,false));
            }
        };

        RVTugasGuru.setAdapter(adapter);
    }

    public void refData()
    {

        if(posSis.equals("guru"))
        {
            if(guruPik.equals("1"))
            {
                btnTambahTugas.setEnabled(false);
                btnTambahTugas.setVisibility(View.GONE);
            }
            else
            {
                btnTambahTugas.setEnabled(true);
                btnTambahTugas.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            btnTambahTugas.setEnabled(false);
            btnTambahTugas.setVisibility(View.GONE);
        }

        if(posSis.equals("murid"))
        {
            refTugasGuru.orderByChild("kelastujuan").equalTo(kelSis).addValueEventListener(new ValueEventListener() {
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
        }
        else
        {
            if(guruPik.equals("0"))
            {
                refTugasGuru.orderByChild("uid").equalTo(myAuth.getUid()).addValueEventListener(new ValueEventListener() {
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
            }
            else
            {
                refTugasGuru.addValueEventListener(new ValueEventListener() {
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
            }

        }

    }
}
