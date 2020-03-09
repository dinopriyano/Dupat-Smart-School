package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TambahTugasPage extends AppCompatActivity {

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

    Date date = new Date();


    String[] fileName;
    String namaFile;

    TextView namaTulis;
    Spinner spKelasTujuan;
    ImageView btnAttach,btnSend;
    FirebaseAuth myAuth;

    ArrayList<Uri> selectedFiles;

    RelativeLayout rlTulisPesan;
    EditText etTulisTugas,mapelTugas;
    StorageReference attachmentFile;

    LazyLoader progBarTambahTugas;
    RelativeLayout bgRegLoadTambahTugas;

    String nama,posisi,gurupiket;

    Toolbar toolbar;
    String TAG = "TambahTugasPage";
    AdapterTambahTugas adapterTambahTugas;
    RecyclerView recyclerView;

    int PICK_FILE_REQUEST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tugas_page);



        bgRegLoadTambahTugas = (RelativeLayout) findViewById(R.id.bgLoadLogTambahTugas);
        progBarTambahTugas = (LazyLoader) findViewById(R.id.progresBarTambahTugas);

        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(150);
        loader.setFirstDelayDuration(150);
        loader.setSecondDelayDuration(150);
        loader.setInterpolator(new LinearInterpolator());

        progBarTambahTugas.addView(loader);
        bgRegLoadTambahTugas.setVisibility(View.GONE);

        namaFile = null;

        PICK_FILE_REQUEST = 10;
        selectedFiles = new ArrayList<>();

        namaTulis = (TextView) findViewById(R.id.namaTulis);
        spKelasTujuan = (Spinner) findViewById(R.id.spKelasTujuan);
        btnAttach = (ImageView) findViewById(R.id.btnAttach);
        btnSend = (ImageView) findViewById(R.id.btnSend);

        rlTulisPesan = (RelativeLayout) findViewById(R.id.rlTulisPesan);
        etTulisTugas = (EditText) findViewById(R.id.etTulisTugas);
        mapelTugas = (EditText) findViewById(R.id.mapelTugas);

        //recyclerview attachment
        recyclerView = findViewById(R.id.rvAttachment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        attachmentFile = FirebaseStorage.getInstance().getReference().child("tugas");
        myAuth = FirebaseAuth.getInstance();



        nama = getIntent().getStringExtra("nama");
        posisi = getIntent().getStringExtra("pos");
        gurupiket =  getIntent().getStringExtra("gurpik");

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaTulis.setText(nama);





        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FileChooser.class);
                intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
                startActivityForResult(intent, PICK_FILE_REQUEST);
            }
        });

        spKelasTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String kelas = adapterView.getItemAtPosition(i).toString();

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        long wak = date.getTime();
                        Timestamp ts = new Timestamp(wak);
                        String timestamp = String.valueOf(wak);

                        bgRegLoadTambahTugas.setVisibility(View.VISIBLE);

                        String nama = namaTulis.getText().toString();
                        final String mapel = mapelTugas.getText().toString();
                        String desc = etTulisTugas.getText().toString();

                        if(kelas.equals("Pilih Kelas"))
                        {
                            Toast.makeText(TambahTugasPage.this, "pilih kelas tujuan terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                        else if(mapel.equals(""))
                        {
                            mapelTugas.setError("mata pelajaran wajib diisi");
                        }
                        else if(desc.equals(""))
                        {
                            etTulisTugas.setError("deskripsi wajib di tulis");
                        }
                        else
                        {



                            if(selectedFiles.size() != 0)
                            {
                                final Map<String,Object> map = new HashMap<>();
                                map.put("namaguru",nama);
                                map.put("kelastujuan",kelas);
                                map.put("mapel",mapel);
                                map.put("timestamp",timestamp);
                                map.put("adalampiran","1");
                                map.put("tanggal",hariIni);
                                map.put("uid",myAuth.getUid());
                                map.put("id",kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp);
                                map.put("keterangan",desc);
                                map.put("lampiran","0");

                                FirebaseDatabase.getInstance().getReference("kelas").child(kelas).child("tugas").child(hariIni).child(kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseDatabase.getInstance().getReference("tugas").child(kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                for(int i = 0;i < selectedFiles.size();i++)
                                                {

                                                    fileName = selectedFiles.get(i).toString().split("\\/");
                                                    for(int j = 0; j<fileName.length;j++)
                                                    {
                                                        namaFile = fileName[j];
                                                    }

                                                    final String b = namaFile;

                                                    final Map<String,Object> map2 = new HashMap<>();
                                                    map2.put("namafile",namaFile);

                                                    attachmentFile.child(kelas).child(hariIni).child(mapel).child(namaFile).putFile(selectedFiles.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                            attachmentFile.child(kelas).child(hariIni).child(mapel).child(b).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                                @Override
                                                                public void onSuccess(StorageMetadata storageMetadata) {
                                                                    map2.put("ukuranfile",storageMetadata.getSizeBytes());

                                                                    attachmentFile.child(kelas).child(hariIni).child(mapel).child(b).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            map2.put("url",uri.toString());
                                                                            Log.d(TAG, "onSuccess: "+namaFile);
                                                                            Log.d(TAG, "onComplete: "+uri.toString());
                                                                            FirebaseDatabase.getInstance().getReference("kelas").child(kelas).child("tugas").child(hariIni).child(kelas+"-"+mapel+"-"+myAuth.getUid()).child("lampiran").push().setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    FirebaseDatabase.getInstance().getReference("tugas").child(kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp).child("lampiran").push().setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            bgRegLoadTambahTugas.setVisibility(View.GONE);
                                                                                            Intent intent = new Intent(TambahTugasPage.this,TugasGuruPage.class);
                                                                                            intent.putExtra("suksesTambahTugas","1");
                                                                                            intent.putExtra("update",true);
                                                                                            finish();
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });

                                                                }
                                                            });



                                                        }
                                                    });

                                                }

                                            }
                                        });


                                    }
                                });

                            }
                            else
                            {
                                final Map<String,Object> map = new HashMap<>();
                                map.put("namaguru",nama);
                                map.put("tanggal",hariIni);
                                map.put("kelastujuan",kelas);
                                map.put("timestamp",timestamp);
                                map.put("mapel",mapel);
                                map.put("uid",myAuth.getUid());
                                map.put("id",kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp);
                                map.put("keterangan",desc);
                                map.put("lampiran","0");
                                map.put("adalampiran","0");

                                FirebaseDatabase.getInstance().getReference("kelas").child(kelas).child("tugas").child(hariIni).child(kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseDatabase.getInstance().getReference("tugas").child(kelas+"-"+mapel+"-"+myAuth.getUid()+"-"+timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                bgRegLoadTambahTugas.setVisibility(View.GONE);

                                                Intent intent = new Intent(TambahTugasPage.this,TugasGuruPage.class);
                                                intent.putExtra("suksesTambahTugas","1");
                                                intent.putExtra("update",true);
                                                finish();
                                                startActivity(intent);


                                            }
                                        });
                                    }
                                });
                            }
                        }


                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rlTulisPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTulisTugas.requestFocus();
            }
        });

    }

    public void insertLampiran(String namaDoc, final String kelas, final String mapel)
    {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE_REQUEST && data != null)
        {
            selectedFiles = data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
            adapterTambahTugas = new AdapterTambahTugas(this,getMyList());
            recyclerView.setAdapter(adapterTambahTugas);
        }
    }

    private ArrayList<SetGetAttachmentTambahTugas> getMyList()
    {
        ArrayList<SetGetAttachmentTambahTugas> models = new ArrayList<>();



        if(selectedFiles.size() != 0)
        {
            for(int i = 0;i < selectedFiles.size();i++)
            {
                SetGetAttachmentTambahTugas att = new SetGetAttachmentTambahTugas();
                att.setPath(selectedFiles.get(i).toString());
                models.add(att);

            }
        }

        return models;
    }


}
