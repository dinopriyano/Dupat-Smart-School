package com.app24announce.dupat.id;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class homePage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private SharedPreferenceConfig preferenceConfig;

    private SharedPreferenceConfig preferenceConfig2;

    String TAG = "homePage";

    RelativeLayout layShrimp;
    FirebaseDatabase database;
    TextToSpeech t1;
    FirebaseUser firebaseUser;
    RelativeLayout bgLogLoadHome,homePage;
    LazyLoader progBarHome;
    CircleImageView fotoProfil;
    String textSound;
    DatabaseReference myRefHome,refTotUser,refTotTelat,refUserPP,refKelas,refJamMapel;
    String kel;
    ArrayList<String> pengumuman = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    RecyclerView rvInformasi;
    CardView cvTulis,cvDaftarTelat,cvdaftarTidakMasuk,cvAboutDev,cvDaftarCekKehadiran,layoutHarian,tambahTugasPage,jadwalPage;
    FirebaseAuth myAuth;
    TextView txtNama,txtPosisi,jumMur,jumTel, lihatProfile,txtLaporan;
    boolean rev;
    ImageButton dropDownMenu;
    String linkPP;

    List<SetGetListInfo> list;
    AdapterPengumumanHomePage adapterPengumumanHomePage;
    private long mLastClickTime = 0;

    TextView txtJam,txtValue,btnToInfo,txtJumAbsen,txtSelInfo,jdlInfo;

    String posisi,guruPiket;

    ShimmerFrameLayout shimmerFrameLayout;

    SimpleDateFormat df;

    String tang;

    String month[] = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
    String hari[] = {"Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
    String hari2[] = {"minggu","senin","selasa","rabu","kamis","jumat","sabtu"};
    GregorianCalendar gc = new GregorianCalendar();
    final String bln = month[gc.get(Calendar.MONTH)];
    final String hri = hari[(gc.get(Calendar.DAY_OF_WEEK))-1];
    final int tgl = gc.get(Calendar.DATE);
    final int year = gc.get(Calendar.YEAR);
    final int jam = gc.get(Calendar.HOUR);
    final int mnt = gc.get(Calendar.MINUTE);
    final int dt = gc.get(Calendar.SECOND);
    final int bul = gc.get(Calendar.MONTH);
    final int pmam = gc.get(Calendar.AM_PM);
    final String hariIni = hri+", "+tgl+" "+bln+" "+year;
    String hri2 = hari2[(gc.get(Calendar.DAY_OF_WEEK))-1];

    //kirim data ke lihat profile page
    String potSis,posSis,namaSis,kelSis,nisSis,nuptkGur,emailSis,passSis,fotoSis,guruPik,ketuaKel;


    //firebase job dispatcher
    FirebaseJobDispatcher jobDispatcher;

    private StorageReference UserProfileImageRef;

    //BootService bootService = new BootService();

    SimpleDateFormat sdfDateTime,sdfDate;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Log.d(TAG, "onCreateHari: "+hri2);

        layShrimp = (RelativeLayout) findViewById(R.id.layShrimp);
        layShrimp.setVisibility(View.VISIBLE);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.Shrimp);
        shimmerFrameLayout.startShimmer();

        txtJumAbsen = (TextView) findViewById(R.id.txtJumAbsen);

        txtSelInfo = (TextView) findViewById(R.id.txtSelInfo);
        jdlInfo = (TextView) findViewById(R.id.jdlInfo);

        dropDownMenu = (ImageButton) findViewById(R.id.menuBtn);
        bgLogLoadHome = (RelativeLayout) findViewById(R.id.bgLoadLogHome);
        progBarHome = (LazyLoader) findViewById(R.id.progresBarHome);
        bgLogLoadHome.setBackgroundResource(R.drawable.bgitem);

        rvInformasi = (RecyclerView) findViewById(R.id.rvInformasi);
        list = new ArrayList<>();

        homePage = (RelativeLayout) findViewById(R.id.homePage);
        //btnToInfo = (TextView) findViewById(R.id.btnToInfo);

        txtLaporan =  (TextView) findViewById(R.id.huaha);
        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRefHome = database.getReference("user").child(myAuth.getUid());
        myRefHome.keepSynced(true);
        refTotUser = database.getReference("user");
        refTotUser.keepSynced(true);
        refTotTelat = database.getReference("terlambat").child(hariIni);
        refTotTelat.keepSynced(true);
        refJamMapel = database.getReference().child("jammapel").child(hri2);
        refJamMapel.keepSynced(true);

        txtJam = (TextView) findViewById(R.id.txtJam);
        txtValue = (TextView) findViewById(R.id.txtValue);
        final Handler handler = new Handler(getMainLooper());

//        startJobSchedule();

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                FirebaseDatabase.getInstance().getReference().child("pengumuman").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
//
//                    String inf = null;
//                    String waktu = null;
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot ds : dataSnapshot.getChildren())
//                        {
//                            inf = ds.child("info").getValue().toString();
//                            waktu = ds.child("time").getValue().toString();
//                        }
//
//                        txtJam.setText(waktu);
//                        txtValue.setText(inf);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                handler.postDelayed(this,1000);
//            }
//        },100);

        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        rvInformasi.setLayoutManager(llm);

        FirebaseDatabase.getInstance().getReference().child("pengumuman").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        SetGetListInfo a = ds.getValue(SetGetListInfo.class);
                        list.add(a);

                    }
                    adapterPengumumanHomePage = new AdapterPengumumanHomePage(homePage.this,list);
                    rvInformasi.setAdapter(adapterPengumumanHomePage);
                }
                else
                {
                    txtSelInfo.setVisibility(View.GONE);
                    jdlInfo.setVisibility(View.GONE);
                    rvInformasi.setVisibility(View.GONE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tang = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                txtJam.setText(tang);
                sdfDateTime = new SimpleDateFormat("HH:mm");
                date = new Date();
                String harJamNi = sdfDateTime.format(date);
                Date harJamTimest = null;
                try {
                    harJamTimest = sdfDateTime.parse(harJamNi);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final long timesHarJamNi = harJamTimest.getTime();

                final Date[] awal = {null};
                final Date[] akhir = {null};
                final Date[] akhirBat = {null};
                final Date[] jamTiga = {null};




                refJamMapel.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long banyakChild = dataSnapshot.getChildrenCount();

                        Log.d(TAG, "onDataChangeAnak: "+banyakChild);
                        if(dataSnapshot.exists())
                        {
                            for(long i = 0;i < banyakChild-1;i++)
                            {

                                Log.d(TAG, "Awal : "+(dataSnapshot                                                                                                                                                                                                                                                                                                                                                                                                                                                                   .child("acara").getValue())+" Akhir : "+(i+2));
                                try {
                                    jamTiga[0] = sdfDateTime.parse("15:00");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long jamT = jamTiga[0].getTime();

                                if(timesHarJamNi > jamT)
                                {
                                    txtValue.setText("Selamat Beristirahat");
                                }
                                else
                                {
                                    try {
                                        awal[0] = sdfDateTime.parse(dataSnapshot.child(String.valueOf((i+1))).child("jam").getValue().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long aa = awal[0].getTime();

                                    try {
                                        akhir[0] = sdfDateTime.parse(dataSnapshot.child(String.valueOf((i+2))).child("jam").getValue().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long bb = akhir[0].getTime();

                                    try {
                                        akhirBat[0] = sdfDateTime.parse(dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("jam").getValue().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long cc = akhirBat[0].getTime();



                                  Log.d(TAG, "onDataChange: "+dataSnapshot.child(String.valueOf(i+1)).child("jam").getValue().toString()+"  "+dataSnapshot.child(String.valueOf(i+2)).child("jam").getValue().toString());



//                                  String a = dataSnapshot.child(String.valueOf(i+1)).child("jam").getValue().toString();
//                                  Log.d(TAG, "onDataChange: "+a);
                                    if(timesHarJamNi >= aa && timesHarJamNi < bb)
                                    {
                                        if(dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("1") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("2") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("3") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("4") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("5") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("6") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("7") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("8") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("9") || dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString().equals("10"))
                                        {
                                            txtValue.setText("Jam Pelajaran Ke-"+dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString());
                                        }
                                        else
                                        {
                                            txtValue.setText(dataSnapshot.child(String.valueOf(i+1)).child("acara").getValue().toString());
                                        }

                                        break;
                                    }
                                    else if(timesHarJamNi >= cc && timesHarJamNi <= jamT)
                                    {

                                        if(dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("1") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("2") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("3") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("4") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("5") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("6") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("7") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("8") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("9") || dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString().equals("10"))
                                        {
                                            txtValue.setText("Jam Pelajaran Ke-"+dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString());
                                        }
                                        else
                                        {
                                            txtValue.setText(dataSnapshot.child(String.valueOf(dataSnapshot.getChildrenCount())).child("acara").getValue().toString());
                                        }

                                        break;
                                    }
//                                else
//                                {
//                                    txtValue.setText("Selamat Beristirahat");
//                                    break;
//                                }
                                }



                            }
                        }
                        else
                        {
                            txtValue.setText("Selamat Beristirahat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                handler.postDelayed(this,1000);
            }
        },100);


        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(150);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());

        progBarHome.addView(loader);
        bgLogLoadHome.setVisibility(View.VISIBLE);


        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        txtNama = (TextView) findViewById(R.id.profNama);
        txtPosisi = (TextView) findViewById(R.id.profPosisi);
        jumMur = (TextView) findViewById(R.id.txtJumMur);
        jumTel = (TextView) findViewById(R.id.txtJumTel);
        final int[] countUserMurid = {0};
        final int[] countUserTelat = {0};
        cvDaftarTelat = (CardView) findViewById(R.id.cvDaftarTelat);
        cvdaftarTidakMasuk = (CardView) findViewById(R.id.cvDaftarTidakMasuk);
        cvAboutDev = (CardView) findViewById(R.id.cvAboutDev);
        cvDaftarCekKehadiran = (CardView) findViewById(R.id.cvDaftarCekKehadiran);
        layoutHarian = (CardView) findViewById(R.id.layHarian);
        jadwalPage = (CardView) findViewById(R.id.jadwalPage);
        tambahTugasPage = (CardView) findViewById(R.id.tambahTugasPage);

        lihatProfile = (TextView) findViewById(R.id.nanani);



        fotoProfil = (CircleImageView) findViewById(R.id.fotoProfile);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Image Profile").child(myAuth.getUid()+".jpg");



        if(firebaseUser != null)
        {
//            Toast.makeText(this, "user ada", Toast.LENGTH_SHORT).show();
            //startService(new Intent(homePage.this,MyService.class));
        }



        if(getIntent().getExtras() != null)
        {
            boolean up = getIntent().getExtras().getBoolean("update");
            if(up)
            {
                String sukses = getIntent().getExtras().getString("suksesTambahFoto");
                if(sukses.equals("1"))
                {
                    Snackbar.make(findViewById(R.id.homePage),"berhasil menambahkan foto", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
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

                posisi = pos;
                guruPiket = gurupiket;

                if(pos.equals("guru"))
                {

                    if(gurupiket.equals("1"))
                    {
                        txtPosisi.setText("Guru Piket");
                        cvdaftarTidakMasuk.setVisibility(View.GONE);
                        cvDaftarCekKehadiran.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cvdaftarTidakMasuk.setVisibility(View.GONE);
                        txtPosisi.setText("Guru");
                        cvDaftarCekKehadiran.setVisibility(View.GONE);
                    }
                }

                txtNama.setText(nama);
                if(pos.equals("murid"))
                {
                    layoutHarian.setVisibility(View.GONE);
                    txtLaporan.setVisibility(View.GONE);
                    cvDaftarTelat.setVisibility(View.GONE);
                    cvDaftarCekKehadiran.setVisibility(View.GONE);

                    if(ketu.equals("0"))
                    {
                        txtPosisi.setText("Murid");
                        cvdaftarTidakMasuk.setVisibility(View.GONE);
                    }

                    else {
                        txtPosisi.setText("Ketua Kelas");
                        cvdaftarTidakMasuk.setVisibility(View.VISIBLE);
                    }
                }
                if(pp.equals("0"))
                {
                    startActivity(new Intent(homePage.this,InsertFoto.class));
                }
                else
                {
                    //buat retrieve foto ke image view online only
//                    try {
//                        final File file = File.createTempFile("image","jpg");
//                        UserProfileImageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                Bitmap bitmap = BitmapFactory.decod
//                                eFile(file.getAbsolutePath());
//                    fotoProfil.setImageBitmap(bitmap);
//                                Picasso.get().load(String.valueOf(bitmap)).into(fotoProfil);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(homePage.this, "gagal memuat foto profil", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    //retrieve foto pake picasso bisa di buka waktu offline
                    Picasso.get().load(pp).placeholder(R.drawable.student).error(R.drawable.student).memoryPolicy(MemoryPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(fotoProfil, new Callback() {
                        @Override
                        public void onSuccess() {

                            layShrimp.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
                bgLogLoadHome.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                bgLogLoadHome.setVisibility(View.INVISIBLE);
            }
        });



        refTotUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    countUserMurid[0] = (int) dataSnapshot.getChildrenCount();
                    jumMur.setText(Integer.toString(countUserMurid[0]));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        btnToInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(homePage.this, pengumumanPage.class);
//                startActivity(intent);
//            }
//        });

        refTotTelat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    countUserTelat[0] = (int) dataSnapshot.getChildrenCount();
                    jumTel.setText(Integer.toString(countUserTelat[0]));
                }
                else
                {
                    jumTel.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtSelInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,pengumumanPage.class);
                startActivity(intent);
            }
        });

        ImageView menu = findViewById(R.id.menuBtn);

        registerForContextMenu(menu);



        preferenceConfig2 = new SharedPreferenceConfig(getApplicationContext());

        CardView pengumuman = (CardView) findViewById(R.id.riwayatPage);
        pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(homePage.this, pengumumanPage.class));
            }
        });

        cvDaftarTelat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(homePage.this, DaftarTelatPage.class);
                intent.putExtra("posisi",posisi);
                intent.putExtra("gurupiket",guruPiket);
                startActivity(intent);


            }
        });

        cvdaftarTidakMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                final Intent intent = new Intent(homePage.this,DaftarTidakMasukKetuaKelas.class);

                refKelas = FirebaseDatabase.getInstance().getReference().child("user").child(myAuth.getUid());
                refKelas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        kel = dataSnapshot.child("kelas").getValue().toString();
                        intent.putExtra("kelas", kel);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        getCountAbsen();

        cvAboutDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(homePage.this,AboutDevPage.class));
            }
        });

        cvDaftarCekKehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(homePage.this,ListSudahInputKehadiranPage.class));
            }
        });

        tambahTugasPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(homePage.this,TugasGuruPage.class);
                intent.putExtra("posisiNya",posSis);
                intent.putExtra("guruPiketNya",guruPiket);
                startActivity(intent);
            }
        });

        lihatProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();

//                posSis = pos;
//                kelSis = kelas;
//                nisSis = nis;
//                namaSis = nama;
//                potSis = pp;

                Intent intent = new Intent(homePage.this,LihatProfilPage.class);
                intent.putExtra("posSis",posSis);
                intent.putExtra("kelSis",kelSis);
                intent.putExtra("nisSis",nisSis);
                intent.putExtra("namaSis",namaSis);
                intent.putExtra("potSis",potSis);
                intent.putExtra("nuptkGur",nuptkGur);
                intent.putExtra("emailSis",emailSis);
                intent.putExtra("passSis",passSis);
                intent.putExtra("fotoSis",fotoSis);
                intent.putExtra("guruPik",guruPik);
                intent.putExtra("ketuaKel",ketuaKel);
                startActivity(intent);
            }
        });

        jadwalPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this, JadwalPage.class);
                intent.putExtra("posSis",posSis);
                intent.putExtra("kelSis",kelSis);
                intent.putExtra("nisSis",nisSis);
                intent.putExtra("namaSis",namaSis);
                intent.putExtra("potSis",potSis);
                intent.putExtra("nuptkGur",nuptkGur);
                intent.putExtra("emailSis",emailSis);
                intent.putExtra("passSis",passSis);
                intent.putExtra("fotoSis",fotoSis);
                intent.putExtra("guruPik",guruPik);
                intent.putExtra("ketuaKel",ketuaKel);
                startActivity(intent);
            }
        });



        dropDownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(homePage.this, view);
                popup.setOnMenuItemClickListener(homePage.this);
                popup.inflate(R.menu.layout_menu);
                popup.show();
            }
        });


    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_logout:

                preferenceConfig2.writeLoginStatus(false);
                //stopService(new Intent(this, MyService.class));
                myAuth.signOut();
                startActivity(new Intent(this, loginPage.class));
                finish();


                return true;
            case R.id.menu_profile:
                Intent intent = new Intent(homePage.this,LihatProfilPage.class);
                intent.putExtra("posSis",posSis);
                intent.putExtra("kelSis",kelSis);
                intent.putExtra("nisSis",nisSis);
                intent.putExtra("namaSis",namaSis);
                intent.putExtra("potSis",potSis);
                intent.putExtra("nuptkGur",nuptkGur);
                intent.putExtra("emailSis",emailSis);
                intent.putExtra("passSis",passSis);
                intent.putExtra("fotoSis",fotoSis);
                intent.putExtra("guruPik",guruPik);
                intent.putExtra("ketuaKel",ketuaKel);
                startActivity(intent);
                return true;
            case R.id.email_contact:
                Intent em = new Intent("android.intent.action.VIEW", Uri.parse("mailto:dupatindonesia@gmail.com"));
                startActivity(em);
                return true;
            case R.id.wa_contact:
                Intent wa = new Intent("android.intent.action.VIEW", Uri.parse("https://wa.me/+6282122420245"));
                startActivity(wa);
                return true;
            default:
                return false;
        }


    }





    private boolean exit = false;
    Handler handler = new Handler();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if (exit){
            finishAffinity();
            // finish activity
        }else{
            Toasty.custom(this,"Tekan Sekali Lagi Untuk Keluar",R.drawable.ic_info_outline_white_24dp, R.color.ToastyColor,1500,true,true).show();
            exit = true;
            //handler.postDelayed( {exit = false}, 3 * 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            },3000);
            }
    }


    public void startJobSchedule()
    {
        ComponentName componentName = new ComponentName(this, JobServiceNotif.class);
        JobInfo info = new JobInfo.Builder(123,componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resltCode = scheduler.schedule(info);
        if(resltCode == JobScheduler.RESULT_SUCCESS)
        {
            Log.d(TAG, "job scheduled");
        }
        else
        {
            Log.d(TAG, "job scheduled failed");
        }
    }

    public void getCountAbsen()
    {
        FirebaseDatabase.getInstance().getReference().child("kehadiran").child(hariIni).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int child = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                txtJumAbsen.setText(String.valueOf(child));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void jobDispatcher()
    {


    }

}
