package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DaftarTelatPage extends AppCompatActivity {

    DatabaseReference myRef;
    ArrayList<SiswaTerlambat> list;
    List<SiswaTerlambat> siswaList;
    FloatingActionMenu btnTambahTelat;
    RelativeLayout bgGelap,layoutUpdateDeleteSiswaTelat;
    Button tutupPopup,tambahSiswaTelat;
    LinearLayout layoutInsert;
    ListView siswa;
    ImageView btnTutupUDTelat;
    private ArrayAdapter<String> kumKelas;
    private Spinner spinKelas;
    Animation fromsmall;
    ImageButton btnBack;
    EditText namaTelat,keteranganTelat,nisTelat;

    Toolbar toolbar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_telat_page);

        siswa = (ListView) findViewById(R.id.listTerlambat);
        siswaList = new ArrayList<>();

        rlGaAdaData = (RelativeLayout) findViewById(R.id.rlGaAdaData);


        btnTambahTelat = (FloatingActionMenu) findViewById(R.id.btnTambahTelat);
        bgGelap = (RelativeLayout) findViewById(R.id.bgGelap);
        layoutInsert = (LinearLayout) findViewById(R.id.isiInsert);
        tutupPopup = (Button) findViewById(R.id.btnTutupPopup);
        tambahSiswaTelat = (Button) findViewById(R.id.btnTambahSiswaTelat);
        namaTelat = (EditText) findViewById(R.id.namaTelat);
        keteranganTelat = (EditText) findViewById(R.id.keteranganTelat);
        btnTutupUDTelat = (ImageView) findViewById(R.id.btnCloseUD);
        nisTelat = (EditText) findViewById(R.id.nisTelat);
        layoutUpdateDeleteSiswaTelat = (RelativeLayout) findViewById(R.id.isiUpdateDeleteSiswa);
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall);
        layoutInsert.setAlpha(0);
        layoutUpdateDeleteSiswaTelat.setAlpha(0);
        btnTutupUDTelat.setAlpha(0);

        toolbar = findViewById(R.id.tb);


        spinKelas = (Spinner) findViewById(R.id.spKelas);
        kumKelas = new ArrayAdapter<String>(DaftarTelatPage.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.kelas));
        kumKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKelas.setAdapter(kumKelas);

        final String hariIni = hri+", "+tgl+" "+bln+" "+year;

        myRef = FirebaseDatabase.getInstance().getReference("terlambat").child(hariIni);
        myRef.keepSynced(true);

        rlGaAdaData.setVisibility(View.GONE);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String posisi = getIntent().getStringExtra("posisi");
        String guruPiket = getIntent().getStringExtra("gurupiket");
        if(posisi.equals("guru") && guruPiket.equals("0"))
        {
            btnTambahTelat.setVisibility(View.GONE);
        }

        spinKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String kelas = adapterView.getItemAtPosition(i).toString();

                tambahSiswaTelat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String nama = namaTelat.getText().toString();
                        final String keterangan = keteranganTelat.getText().toString();
                        final String nis = nisTelat.getText().toString();
                        if(nama.equals(""))
                        {
                            namaTelat.setError("nama wajib diisi !");
                        }
                        else if(keterangan.equals(""))
                        {
                            keteranganTelat.setError("keterangan wajib diisi !");
                        }

                        else if(nis.equals(""))
                        {
                            nisTelat.setError("NIS wajib diisi !");
                        }

                        else if(kelas.equals("Pilih Kelas"))
                        {
                            Toast.makeText(DaftarTelatPage.this,"Pilih Kelas Terlebih dahulu",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            final AdapterTambahTerlambat tambahTelat = new AdapterTambahTerlambat(kelas,keterangan,nama,nis);
                            FirebaseDatabase.getInstance().getReference("terlambat").child(hariIni).child(nis).setValue(tambahTelat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    namaTelat.setText("");
                                    keteranganTelat.setText("");
                                    nisTelat.setText("");
                                    spinKelas.setSelection(0);
                                    Snackbar.make(findViewById(R.id.pageDaftarTelat),"berhasil menambahkan data siswa yang terlambat", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
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


        siswa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SiswaTerlambat siswaTerlambat = siswaList.get(i);
                showUpdateDeleteDialog(siswaTerlambat.getNis(),siswaTerlambat.getNama(),siswaTerlambat.getKelas(),siswaTerlambat.getKeterangan());
                return false;
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    siswaList.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        SiswaTerlambat siswaTerlambat = ds.getValue(SiswaTerlambat.class);
                        siswaList.add(siswaTerlambat);
                    }

                    if(dataSnapshot.exists())
                    {
                        int totalTelat = (int) dataSnapshot.getChildrenCount();
                        rlGaAdaData.setVisibility(View.GONE);
                    }
                    else
                    {
                        rlGaAdaData.setVisibility(View.VISIBLE);
                    }

                AdapterListTerlambat adapter = new AdapterListTerlambat(DaftarTelatPage.this,siswaList);
                siswa.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnTambahTelat.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgGelap.setVisibility(View.VISIBLE);
                layoutInsert.setVisibility(View.VISIBLE);
                layoutInsert.setAlpha(1);
                btnTutupUDTelat.setVisibility(View.GONE);
                layoutInsert.startAnimation(fromsmall);
            }
        });

        tutupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgGelap.setVisibility(View.GONE);
                layoutInsert.setVisibility(View.GONE);
            }
        });

        btnTutupUDTelat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutUpdateDeleteSiswaTelat.setVisibility(View.GONE);
                bgGelap.setVisibility(View.GONE);
                btnTutupUDTelat.setVisibility(View.GONE);
            }
        });




    }

    private void showUpdateDeleteDialog(final String nisTelat, final String namaTelat, final String kelasTelat, final String keteranganTelat)
    {

        bgGelap.setVisibility(View.VISIBLE);
        layoutUpdateDeleteSiswaTelat.setAlpha(1);
        layoutUpdateDeleteSiswaTelat.startAnimation(fromsmall);

        layoutUpdateDeleteSiswaTelat.setVisibility(View.VISIBLE);

        btnTutupUDTelat.setAlpha(1);
        btnTutupUDTelat.startAnimation(fromsmall);
        btnTutupUDTelat.setVisibility(View.VISIBLE);

        final EditText etNama = (EditText)findViewById(R.id.namaTelatUD);
        final Spinner spKelas = (Spinner) findViewById(R.id.spKelasUD);
        final EditText etKeterangan = (EditText) findViewById(R.id.keteranganTelatUD);
        final EditText etNis = (EditText) findViewById(R.id.nisTelatUD);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdateUD);
        final Button btnDelete = (Button) findViewById(R.id.btnDeleteUD);
        final TextView txtProfilNis = (TextView) findViewById(R.id.txtProfil);

        txtProfilNis.setText("Profil "+nisTelat);
        etNama.setText(namaTelat);
        etKeterangan.setText(keteranganTelat);
        etNis.setText(nisTelat);
        String[] kel = getResources().getStringArray(R.array.kelas);
        spKelas.setSelection(Arrays.asList(kel).indexOf(kelasTelat));
        final String[] nisUbah = {nisTelat};

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(nisTelat).removeValue();
                String hariIni = hri+", "+tgl+" "+bln+" "+year;
                final AdapterTambahTerlambat tambahTelat = new AdapterTambahTerlambat(spKelas.getSelectedItem().toString(),etKeterangan.getText().toString(),etNama.getText().toString(),etNis.getText().toString());
                FirebaseDatabase.getInstance().getReference("terlambat").child(hariIni).child(etNis.getText().toString()).setValue(tambahTelat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.pageDaftarTelat),"berhasil mengubah data siswa yang terlambat", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                        nisUbah[0] = etNis.getText().toString();
                        txtProfilNis.setText("Profil "+nisUbah[0]);
                    }
                });

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(nisUbah[0]).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.pageDaftarTelat),"berhasil menghapus data siswa yang terlambat", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                        txtProfilNis.setText("");
                        etNama.setText("");
                        etKeterangan.setText("");
                        etNis.setText("");
                        spKelas.setSelection(0);
                    }
                });
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();


    }
}
