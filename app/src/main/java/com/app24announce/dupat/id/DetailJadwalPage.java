package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.util.ArrayUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailJadwalPage extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Toolbar toolbar;
    RecyclerView myJadwal;

    //layout popup tambah mapel
    RelativeLayout bgGelap;
    LinearLayout isiPopup;
    EditText txtMapelPopup,txtHariPopup;
    Button btnTambahPopup,btnTutupPopup;
    Animation fromsmall;
    String hariForAdd;

    String posisi,gurupiket,ketuakelas,kelassis;

    EditText etStartTime,etEndTime;

    private ArrayList<SetGetDetailJadwalPage> arrayList;
    private FirebaseRecyclerOptions<SetGetDetailJadwalPage> options;
    private FirebaseRecyclerAdapter<SetGetDetailJadwalPage, AdapterDetailJadwalPage> adapter;

    int ur;
    boolean jamBentrok;
    boolean insert;

    String timePickerFor;

    DatabaseReference myRefJadwal;
    FirebaseAuth myAuth;

    String kelas;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal_page);

        posisi = getIntent().getStringExtra("posisi");
        ketuakelas = getIntent().getStringExtra("ketuakelas");
        gurupiket = getIntent().getStringExtra("gurupiket");
        kelassis = getIntent().getStringExtra("kelassis");

        //deklarasi layout pop up tambah mapel
        bgGelap = (RelativeLayout) findViewById(R.id.bgGelap);
        isiPopup = (LinearLayout) findViewById(R.id.isiPopup);
        txtMapelPopup = (EditText) findViewById(R.id.txtMapelPopup);
        btnTambahPopup = (Button) findViewById(R.id.btnTambahPopup);
        btnTutupPopup = (Button) findViewById(R.id.btnTutupPopup);
        txtHariPopup = (EditText) findViewById(R.id.txtHariPopup);
        bgGelap.setVisibility(View.GONE);
        isiPopup.setVisibility(View.GONE);
        isiPopup.setAlpha(0);
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall);


        etStartTime = (EditText) findViewById(R.id.startTime);
        etEndTime = (EditText) findViewById(R.id.endTime);


        kelas = getIntent().getStringExtra("kelas");

        String TAG = "DetailJadwalPage";



        toolbar = findViewById(R.id.tb);
        toolbar.setTitle("Jadwal "+kelas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myJadwal = (RecyclerView) findViewById(R.id.myJadwal);

        myJadwal.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myJadwal.setLayoutManager(linearLayoutManager);


        myAuth = FirebaseAuth.getInstance();
        myRefJadwal = FirebaseDatabase.getInstance().getReference().child("jadwal").child(kelas).child("jadwal");
        myRefJadwal.keepSynced(true);

        arrayList = new ArrayList<SetGetDetailJadwalPage>();
        options = new FirebaseRecyclerOptions.Builder<SetGetDetailJadwalPage>().setQuery(myRefJadwal.orderByChild("hari"),SetGetDetailJadwalPage.class).build();



        adapter = new FirebaseRecyclerAdapter<SetGetDetailJadwalPage, AdapterDetailJadwalPage>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterDetailJadwalPage adapterDetailJadwalPage, int i, @NonNull SetGetDetailJadwalPage setGetDetailJadwalPage) {
                adapterDetailJadwalPage.txtHari.setText(setGetDetailJadwalPage.getHari());

//                Log.d(TAG, setGetDetailJadwalPage.getHari());


                myRefJadwal.child(setGetDetailJadwalPage.getHari()).child("mapel").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        RecyclerView rvMapel = adapterDetailJadwalPage.myJadwal;
                        RecyclerView.LayoutManager layoutManager;
                        AdapterInsideDetailJadwal adapterInsideDetailJadwal;
                        String mapelList[];

                        if(posisi.equals("guru"))
                        {
                            if(gurupiket.equals("1"))
                            {
                                adapterDetailJadwalPage.txtTambah.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                adapterDetailJadwalPage.txtTambah.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            if(ketuakelas.equals("1"))
                            {
                                if(kelassis.equals(kelas))
                                {
                                    adapterDetailJadwalPage.txtTambah.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    adapterDetailJadwalPage.txtTambah.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                adapterDetailJadwalPage.txtTambah.setVisibility(View.GONE);
                            }
                        }


                        if(dataSnapshot.getChildrenCount() >= 0)
                        {

                            List<SetGetInsideDetailJadwal> list = new ArrayList<>();
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                rvMapel.setLayoutManager(new LinearLayoutManager(DetailJadwalPage.this));
                                rvMapel.setHasFixedSize(true);

                                SetGetInsideDetailJadwal setGetInsideDetailJadwal = ds.getValue(SetGetInsideDetailJadwal.class);

                                list.add(setGetInsideDetailJadwal);


                            }
                            adapterInsideDetailJadwal = new AdapterInsideDetailJadwal(list,DetailJadwalPage.this, ketuakelas, posisi, gurupiket,kelassis,kelas);
                            rvMapel.setAdapter(adapterInsideDetailJadwal);

                        }




                        adapterDetailJadwalPage.txtTambah.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bgGelap.setVisibility(View.VISIBLE);
                                isiPopup.setVisibility(View.VISIBLE);
                                isiPopup.setAlpha(1);
                                isiPopup.startAnimation(fromsmall);
                                txtHariPopup.setText(setGetDetailJadwalPage.getHari());
                                hariForAdd = setGetDetailJadwalPage.getHari();
                                getUrutan(hariForAdd);

//                                Toast.makeText(DetailJadwalPage.this, String.valueOf(ur), Toast.LENGTH_SHORT).show();
                            }
                        });

                        btnTutupPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtMapelPopup.setText("");
                                etStartTime.setText("");
                                etEndTime.setText("");
                                isiPopup.setVisibility(View.GONE);
                                bgGelap.setVisibility(View.GONE);
                            }
                        });

                        etStartTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DialogFragment timePicker = new TimePickerJadwal();
                                timePickerFor = "startTime";
                                timePicker.show(getSupportFragmentManager(),"Waktu Mulai");


                            }
                        });

                        etEndTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment timePicker = new TimePickerJadwal();
                                timePickerFor = "endTime";
                                timePicker.show(getSupportFragmentManager(),"Waktu Selesai");
                            }
                        });


                        btnTambahPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                insert = true;

                                //Toast.makeText(DetailJadwalPage.this, String.valueOf(ur), Toast.LENGTH_SHORT).show();
                                if(txtMapelPopup.getText().toString().equals(""))
                                {
                                    txtMapelPopup.setError("mapel wajib diisi");
                                }
                                else if(etStartTime.getText().toString().equals(""))
                                {
                                    etStartTime.setError("waktu awal wajib diisi");
                                }
                                else if(etEndTime.getText().toString().equals(""))
                                {
                                    etEndTime.setError("waktu awal wajib diisi");
                                }
                                else
                                {


                                    String push = myRefJadwal.child(hariForAdd).child("mapel").push().getKey();


                                    Date date = new Date();
                                    long wak = date.getTime();
                                    Timestamp ts = new Timestamp(wak);
                                    String timestamp = String.valueOf(wak);

                                    final Map<String,Object> map = new HashMap<>();
                                    map.put("id",push);
                                    map.put("waktuawal",etStartTime.getText().toString());
                                    map.put("waktuakhir",etEndTime.getText().toString());
                                    map.put("urutan",ur+1);
                                    map.put("hari",hariForAdd);
                                    map.put("mapel",txtMapelPopup.getText().toString());
                                    map.put("timestamp",timestamp);
                                    map.put("kelas",kelas);

                                    //get timestamp from input
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                    Date dateAwal = null;
                                    Date dateAkhir = null;
                                    try {
                                        dateAwal = sdf.parse(etStartTime.getText().toString());
                                        dateAkhir = sdf.parse(etEndTime.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long jamMul = dateAwal.getTime();
                                    long jamAkh = dateAkhir.getTime();


                                    //Toast.makeText(DetailJadwalPage.this, dateAwal+" timestamp : "+jamMul+ "  "+dateAkhir+" timestamp : "+jamAkh, Toast.LENGTH_SHORT).show();

                                    myRefJadwal.child(hariForAdd).child("mapel").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getChildrenCount() > 0)
                                            {
                                                jamBentrok = false;
                                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                                {
                                                    Date dateMulaiDB = null;
                                                    Date dateAkhirDB = null;

                                                    try {
                                                        dateMulaiDB = sdf.parse(ds.child("waktuawal").getValue().toString());
                                                        dateAkhirDB = sdf.parse(ds.child("waktuakhir").getValue().toString());
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    long timestampDBawal = dateMulaiDB.getTime();
                                                    long timestampDBakhir = dateAkhirDB.getTime();

                                                    Log.d(TAG, "waktu : "+dateMulaiDB+" timestamp : "+timestampDBawal);
                                                    Log.d(TAG, "waktu : "+dateAkhirDB+" timestamp : "+timestampDBakhir);

                                                    if(jamMul>timestampDBawal && jamMul<timestampDBakhir)
                                                    {
                                                        jamBentrok = true;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                Log.d(TAG, "Ga Ada Data");
                                                jamBentrok = false;
                                            }

                                            if(insert)
                                            {
                                                if(jamMul>jamAkh)
                                                {
                                                    bgGelap.setVisibility(View.GONE);
                                                    isiPopup.setVisibility(View.GONE);
                                                    isiPopup.setAlpha(0);

                                                    new SweetAlertDialog(DetailJadwalPage.this,SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Kesalahan")
                                                            .setContentText("Waktu tidak boleh mundur")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                    bgGelap.setVisibility(View.VISIBLE);
                                                                    isiPopup.setVisibility(View.VISIBLE);
                                                                    isiPopup.setAlpha(1);
                                                                    isiPopup.startAnimation(fromsmall);
                                                                }
                                                            })
                                                            .show();
                                                }

                                                else if(jamBentrok)
                                                {
                                                    bgGelap.setVisibility(View.GONE);
                                                    isiPopup.setVisibility(View.GONE);
                                                    isiPopup.setAlpha(0);
                                                    new SweetAlertDialog(DetailJadwalPage.this,SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Kesalahan")
                                                            .setContentText("Jam pelajaran berbenturan")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                    bgGelap.setVisibility(View.VISIBLE);
                                                                    isiPopup.setVisibility(View.VISIBLE);
                                                                    isiPopup.setAlpha(1);
                                                                    isiPopup.startAnimation(fromsmall);
                                                                }
                                                            })
                                                            .show();
                                                    //Toast.makeText(DetailJadwalPage.this, "Jam Bentrok", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {

                                                    myRefJadwal.child(hariForAdd).child("mapel").child(push).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG, "onDataChange: "+map);
                                                            txtMapelPopup.setText("");
                                                            etStartTime.setText("");
                                                            etEndTime.setText("");
                                                            Snackbar.make(findViewById(R.id.DetailJadwalPage),"berhasil menambahkan mapel", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                                                            insert = false;
                                                        }
                                                    });


                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




                                }
                            }
                        });





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public AdapterDetailJadwalPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDetailJadwalPage(LayoutInflater.from(DetailJadwalPage.this).inflate(R.layout.row_recyclerview_detail_jadwal_page,parent,false));
            }
        };

        myJadwal.setAdapter(adapter);



    }



    public void getUrutan(String hari)
    {

        myRefJadwal.child(hari).child("mapel").orderByChild("urtan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    ur = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(Integer.parseInt(ds.child("urutan").getValue().toString())>ur)
                        {
                            ur = Integer.parseInt(ds.child("urutan").getValue().toString());
                        }

                    }

                }
                else
                {

                    ur = 0;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String a = String.valueOf(i);
        String b = String.valueOf(i1);
        String waktu = a+":"+b;
        Date date = null;
        try {
            date = sdf.parse(waktu);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String thisJam = sdf.format(date);

        //Toast.makeText(this, thisJam, Toast.LENGTH_SHORT).show();

        if(timePickerFor.equals("startTime"))
        {
            etStartTime.setText(thisJam);
            timePickerFor = null;
        }
        else if(timePickerFor.equals("endTime"))
        {
            etEndTime.setText(thisJam);
            timePickerFor = null;
        }
    }
}
