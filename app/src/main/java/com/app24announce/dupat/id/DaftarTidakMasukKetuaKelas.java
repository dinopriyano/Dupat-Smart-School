package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class DaftarTidakMasukKetuaKelas extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    private DatabaseReference myRef,refKelas, refSudahInputKehadiran;

    FloatingActionButton btnShowGaMasuk;
    RelativeLayout rvBgInsertGaMasuk,isiUpdateDeleteGaMasuk;
    FloatingActionMenu floatingActionMenu;
    LinearLayout isiInsertKehadiran;
    Button btnTutupPopup,btnTambahSiswaGaMasuk;
    Animation fromsmall;
    Spinner spKetGaMasuk;
    EditText namaGaMasuk,nisGaMasuk;
    FirebaseAuth myAuth;
    String kel;
    CircleImageView btnCloseUD;
    String nisGanti;
    String hm;
    String kelkel;

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

    private ArrayList<SetGetKehadiran> arrayList;
    private FirebaseRecyclerOptions<SetGetKehadiran> options;
    private FirebaseRecyclerAdapter<SetGetKehadiran, AdapterKehadiran> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_tidak_masuk_ketua_kelas);

        rlGaAdaData = (RelativeLayout) findViewById(R.id.rlGaAdaData);
        rlGaAdaData.setVisibility(View.VISIBLE);

        kelkel = getIntent().getStringExtra("kelas");

        btnShowGaMasuk = (FloatingActionButton) findViewById(R.id.btnShowGaMasuk);

        rvBgInsertGaMasuk = (RelativeLayout) findViewById(R.id.rvBgInsertGaMasuk);
        isiInsertKehadiran = (LinearLayout) findViewById(R.id.isiInsertKehadiran);
        btnTutupPopup = (Button) findViewById(R.id.btnTutupPopup);
        btnTambahSiswaGaMasuk = (Button) findViewById(R.id.btnTambahSiswaGaMasuk);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.btnTambahGaMasuk);
        spKetGaMasuk = (Spinner) findViewById(R.id.spKetGaMasuk);
        namaGaMasuk = (EditText) findViewById(R.id.namaGaMasuk);
        nisGaMasuk = (EditText) findViewById(R.id.nisGaMasuk);
        myAuth = FirebaseAuth.getInstance();
        isiUpdateDeleteGaMasuk = (RelativeLayout) findViewById(R.id.isiUpdateDeleteGaMasuk);
        btnCloseUD = (CircleImageView) findViewById(R.id.btnCloseUD);

        isiUpdateDeleteGaMasuk.setAlpha(0);
        btnCloseUD.setAlpha(0);


        recyclerView = (RecyclerView) findViewById(R.id.RVKehadiran);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String uid = myAuth.getUid();


        refKelas = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        refKelas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kel = dataSnapshot.child("kelas").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String a = kel;



        final String hariIni = hri+", "+tgl+" "+bln+" "+year;

        myRef = FirebaseDatabase.getInstance().getReference().child("kehadiran").child(hariIni);
        refSudahInputKehadiran = FirebaseDatabase.getInstance().getReference().child("kehadiran").child("sudahinput").child(hariIni);
        myRef.keepSynced(true);

        arrayList = new ArrayList<SetGetKehadiran>();
        options = new FirebaseRecyclerOptions.Builder<SetGetKehadiran>().setQuery(myRef.orderByChild("kelas").equalTo(kelkel),SetGetKehadiran.class).build();

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spKetGaMasuk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String ket = adapterView.getItemAtPosition(i).toString();

                btnTambahSiswaGaMasuk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nama = namaGaMasuk.getText().toString();
                        String nis = nisGaMasuk.getText().toString();

                        if(ket.equals("Pilih Keterangan"))
                        {
                            Toast.makeText(DaftarTidakMasukKetuaKelas.this, "pilih keterangan terlebih dahulu!", Toast.LENGTH_SHORT).show();
                        }

                        else if(nama.equals(""))
                        {
                            namaGaMasuk.setError("nama wajib diisi!");
                        }

                        else if(nis.equals(""))
                        {
                            nisGaMasuk.setError("nis wajib diisi!");
                        }

                        else
                        {
                            Map<String, Object> map = new HashMap<>();
                            map.put("kelas",kel);
                            map.put("keterangan",ket);
                            map.put("nama",nama);
                            map.put("nis",nis);
                            myRef.child(nis).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Map<String,Object> map2 = new HashMap<>();
                                    map2.put("kelas",kel);
                                    refSudahInputKehadiran.child(kel).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            namaGaMasuk.setText("");
                                            nisGaMasuk.setText("");
                                            spKetGaMasuk.setSelection(0);
                                            Snackbar.make(findViewById(R.id.pageTambahGaMasuk),"berhasil menambah data", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                                        }
                                    });
                                }
                            });

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        refSudahInputKehadiran.child(kelkel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                if(dataSnapshot2.exists())
                {
                    rlGaAdaData.setVisibility(View.GONE);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists())
                            {
                                refSudahInputKehadiran.child(kelkel).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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



        adapter = new FirebaseRecyclerAdapter<SetGetKehadiran, AdapterKehadiran>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterKehadiran adapterKehadiran, int i, @NonNull final SetGetKehadiran setGetKehadiran) {

                adapterKehadiran.txtKeterangan.setText(setGetKehadiran.getKeterangan());
                adapterKehadiran.txtNama.setText(setGetKehadiran.getNama());
                adapterKehadiran.txtNis.setText(setGetKehadiran.getNis());
//                if(setGetKehadiran.getKeterangan().equals("Alpa"))
//                {
//                    adapterKehadiran.txtNama.setVisibility(View.GONE);
//                }

                adapterKehadiran.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showUpdateDeleteDialog(setGetKehadiran.getKelas(),setGetKehadiran.getKeterangan(),setGetKehadiran.getNama(),setGetKehadiran.getNis());
                        return false;
                    }
                });


            }

            @NonNull
            @Override
            public AdapterKehadiran onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterKehadiran(LayoutInflater.from(DaftarTidakMasukKetuaKelas.this).inflate(R.layout.row_recyclerview_kehadiran,parent,false));
            }
        };


        isiInsertKehadiran.setAlpha(0);
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall);
        recyclerView.setAdapter(adapter);

        btnShowGaMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvBgInsertGaMasuk.setVisibility(View.VISIBLE);
                isiInsertKehadiran.setVisibility(View.VISIBLE);
                isiInsertKehadiran.setAlpha(1);
                isiInsertKehadiran.startAnimation(fromsmall);
                floatingActionMenu.close(true);
            }
        });



        btnTutupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvBgInsertGaMasuk.setVisibility(View.GONE);
                isiInsertKehadiran.setVisibility(View.GONE);
                isiInsertKehadiran.setAlpha(0);

            }
        });

        btnCloseUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvBgInsertGaMasuk.setVisibility(View.GONE);
                isiUpdateDeleteGaMasuk.setVisibility(View.GONE);
                btnCloseUD.setVisibility(View.GONE);
                isiUpdateDeleteGaMasuk.setAlpha(0);
                btnCloseUD.setAlpha(0);
            }
        });

    }

    private void showUpdateDeleteDialog(final String kelas,final String keterangan, final String nama, final String nis)
    {
        rvBgInsertGaMasuk.setVisibility(View.VISIBLE);
        isiUpdateDeleteGaMasuk.setVisibility(View.VISIBLE);
        btnCloseUD.setVisibility(View.VISIBLE);
        rvBgInsertGaMasuk.setAlpha(1);
        isiUpdateDeleteGaMasuk.setAlpha(1);
        btnCloseUD.setAlpha(1);
        isiUpdateDeleteGaMasuk.startAnimation(fromsmall);
        btnCloseUD.startAnimation(fromsmall);

        final EditText etNama = (EditText)findViewById(R.id.namaKehadiranUD);
        final EditText etNis = (EditText)findViewById(R.id.nisKehadiranUD);
        final Spinner spKelas = (Spinner) findViewById(R.id.spKetUD);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdateUD);
        final Button btnDelete = (Button) findViewById(R.id.btnDeleteUD);
        final TextView txtProfilNis = (TextView) findViewById(R.id.txtProfilGaMasuk);

        txtProfilNis.setText("Profil "+nis);
        etNama.setText(nama);
        etNis.setText(nis);
        String[] kel = getResources().getStringArray(R.array.sia_kehadiran);
        spKelas.setSelection(Arrays.asList(kel).indexOf(keterangan));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(nis).removeValue();

                Map<String, Object> mapUpdate = new HashMap<>();
                mapUpdate.put("kelas",kelas);
                mapUpdate.put("keterangan",spKelas.getSelectedItem());
                mapUpdate.put("nama",etNama.getText().toString());
                mapUpdate.put("nis",etNis.getText().toString());

                myRef.child(etNis.getText().toString()).setValue(mapUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.pageTambahGaMasuk),"berhasil mengubah data", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                        nisGanti = etNis.getText().toString();
                        txtProfilNis.setText("Profil "+nisGanti);
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child(nis).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.pageTambahGaMasuk),"berhasil menghapus data", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                        etNama.setText("");
                        etNis.setText("");
                        spKelas.setSelection(0);
                        txtProfilNis.setText("Profil Siswa");
                    }
                });

            }
        });

    }


}
