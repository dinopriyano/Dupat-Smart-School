package com.app24announce.dupat.id;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class pengumumanPage extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth myAuth;
    FloatingActionMenu menuTambahInfo;
    public static boolean reverse = false;
    TextToSpeech t1;
    List<SetGetListInfo> infoList;
    String textSound;
    DatabaseReference myRef, myRefMurid;
    ArrayList<String> pengumuman = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    RecyclerView lv;
    LinearLayoutManager mLayoutManager;
    private FloatingActionMenu btnTambahInfo;
    Animation fromsmall;
    Button tambahInfo, tutupTambahInfo;
    RelativeLayout bgGelap;
    LinearLayout layoutTambahInfo;
    EditText textInfo;
    ImageButton btnBack;

    Toolbar toolbar;


    String month[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    String hari[] = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    GregorianCalendar gc = new GregorianCalendar();
    final String bln = month[gc.get(Calendar.MONTH)];
    String hri = hari[(gc.get(Calendar.DAY_OF_WEEK)) - 1];
    final int tgl = gc.get(Calendar.DATE);
    final int year = gc.get(Calendar.YEAR);
    final int jam = gc.get(Calendar.HOUR);
    final int mnt = gc.get(Calendar.MINUTE);
    final int dt = gc.get(Calendar.SECOND);
    final int pmam = gc.get(Calendar.AM_PM);


    String tang = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;
    private final String NOTIFICATION_CHANNEL_ID = "my_channel_01";


    RelativeLayout rlGaAdaData;

    AdapterListInfo adapterListInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman_page);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });


        database = FirebaseDatabase.getInstance();


        rlGaAdaData = (RelativeLayout) findViewById(R.id.rlGaAdaData);
        rlGaAdaData.setVisibility(View.GONE);


        textInfo = (EditText) findViewById(R.id.textInfo);
        infoList = new ArrayList<>();
        myRef = database.getReference("pengumuman");
        myRef.keepSynced(true);
        lv = (RecyclerView) findViewById(R.id.myListView);
        btnTambahInfo = (FloatingActionMenu) findViewById(R.id.btnTambahInfo);
        fromsmall = AnimationUtils.loadAnimation(this, R.anim.fromsmall);
        bgGelap = (RelativeLayout) findViewById(R.id.bgGelap);
        layoutTambahInfo = (LinearLayout) findViewById(R.id.isiInsertInfo);
        layoutTambahInfo.setAlpha(0);
        tambahInfo = (Button) findViewById(R.id.btnTambahInformasi);
        tutupTambahInfo = (Button) findViewById(R.id.btnTutupPopupInformasi);


        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        lv.setLayoutManager(mLayoutManager);


        myAuth = FirebaseAuth.getInstance();
        myRefMurid = database.getReference("user").child(myAuth.getUid());
        myRefMurid.keepSynced(true);
        menuTambahInfo = (FloatingActionMenu) findViewById(R.id.btnTambahInfo);

        menuTambahInfo.setEnabled(false);
        menuTambahInfo.setVisibility(View.GONE);

        myRefMurid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("nama").getValue().toString();
                String kelas = dataSnapshot.child("kelas").getValue().toString();
                String nis = dataSnapshot.child("nis").getValue().toString();
                String pos = dataSnapshot.child("posisi").getValue().toString();
                String pp = dataSnapshot.child("fotoprofil").getValue().toString();


                if (!pos.equals("murid")) {
                    menuTambahInfo.setEnabled(true);
                    menuTambahInfo.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SetGetListInfo info = ds.getValue(SetGetListInfo.class);
                    infoList.add(info);


                }

                if (dataSnapshot.exists()) {
                    int totalInfo = (int) dataSnapshot.getChildrenCount();
                    rlGaAdaData.setVisibility(View.GONE);
                } else {
                    rlGaAdaData.setVisibility(View.VISIBLE);
                }
                adapterListInfo = new AdapterListInfo(pengumumanPage.this, infoList);
                lv.setAdapter(adapterListInfo);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnTambahInfo.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTambahInfo.setEnabled(false);
                bgGelap.setVisibility(View.VISIBLE);
                layoutTambahInfo.setVisibility(View.VISIBLE);
                layoutTambahInfo.setAlpha(1);
                layoutTambahInfo.startAnimation(fromsmall);
            }
        });

        tambahInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahInformasi();
            }
        });

        tutupTambahInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutTambahInfo.setVisibility(View.GONE);
                bgGelap.setVisibility(View.GONE);
                btnTambahInfo.setEnabled(true);
            }
        });


    }

    public void tambahInformasi() {
        final String info = textInfo.getText().toString();

        if (info.equals("")) {
            Toast.makeText(getApplicationContext(), "Tulis penggumuman terlebih dahulu", Toast.LENGTH_LONG).show();
        } else {
            String sp = "";
            if (pmam == 1) {
                sp = "PM";
            } else {
                sp = "AM";
            }
            String time = tgl + " " + bln + " " + year + " ," + jam + ":" + mnt + ":" + dt + " " + sp;
            Date date = new Date();
            long wak = date.getTime();
            Timestamp ts = new Timestamp(wak);
            String timestamp = String.valueOf(wak);

            final AdapterTambahPenggumuman tambahPeng = new AdapterTambahPenggumuman(info, tang, timestamp);
            myRef.push().setValue(tambahPeng).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(findViewById(R.id.pagePengumuman), "berhasil menambahkan pengumuman", Snackbar.LENGTH_LONG).setAction("Action!", null).show();
                }
            });

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


}
