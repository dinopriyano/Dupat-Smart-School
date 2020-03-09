package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DetailTugasPage extends AppCompatActivity {

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

    long Kb = 1 * 1024;
    long Mb = Kb * 1024;
    long Gb = Mb * 1024;
    long Tb = Gb * 1024;

    Toolbar toolbar;
    TextView mapelDetil,namaGuruTanggalTulis,kelasDetil,descDetil;

    RecyclerView rvLampiranDetail;

    String adalampiran,id,kelastujuan,keterangan,mapel,namaguru,tanggal,uid;

    DatabaseReference refTugas;
    FirebaseAuth myAuth;


    private ArrayList<SetGetDetailTugasPage> arrayList;
    private FirebaseRecyclerOptions<SetGetDetailTugasPage> options;
    private FirebaseRecyclerAdapter<SetGetDetailTugasPage, AdapterDetailTugasPage> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas_page);

        adalampiran = getIntent().getStringExtra("adalampiran");
        id = getIntent().getStringExtra("id");
        kelastujuan = getIntent().getStringExtra("kelastujuan");
        keterangan = getIntent().getStringExtra("keterangan");
        mapel = getIntent().getStringExtra("mapel");
        namaguru = getIntent().getStringExtra("namaguru");
        tanggal = getIntent().getStringExtra("tanggal");
        uid = getIntent().getStringExtra("uid");



        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        myAuth = FirebaseAuth.getInstance();
        refTugas = FirebaseDatabase.getInstance().getReference().child("tugas").child(id).child("lampiran");
        refTugas.keepSynced(true);

        arrayList = new ArrayList<SetGetDetailTugasPage>();
        options = new FirebaseRecyclerOptions.Builder<SetGetDetailTugasPage>().setQuery(refTugas,SetGetDetailTugasPage.class).build();

        rvLampiranDetail = (RecyclerView) findViewById(R.id.rvLampiranDetail);
        //rvLampiranDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvLampiranDetail.setLayoutManager(linearLayoutManager);



        mapelDetil = (TextView) findViewById(R.id.mapelDetil);
        namaGuruTanggalTulis = (TextView) findViewById(R.id.namaGuruTanggalTulis);
        //kelasDetil = (TextView) findViewById(R.id.kelasDetil);
        descDetil = (TextView) findViewById(R.id.descDetil);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mapelDetil.setText(mapel);
        namaGuruTanggalTulis.setText("oleh "+namaguru+". "+tanggal);
        //kelasDetil.setText("untuk "+kelastujuan);
        descDetil.setText(keterangan);


        adapter = new FirebaseRecyclerAdapter<SetGetDetailTugasPage, AdapterDetailTugasPage>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterDetailTugasPage adapterDetailTugasPage, int i, @NonNull final SetGetDetailTugasPage setGetDetailTugasPage) {
                adapterDetailTugasPage.namaFile.setText(setGetDetailTugasPage.getNamafile());

                String ukuranString= null;
                long uk = setGetDetailTugasPage.getUkuranfile();

                if(uk < Kb)
                {
                    ukuranString = String.valueOf(uk)+" bytes";
                }
                else if(uk >= Kb && uk < Mb)
                {
                    ukuranString = String.valueOf(uk/Kb)+" Kb";
                }
                else if(uk >= Mb && uk < Gb)
                {
                    ukuranString = String.valueOf(uk/Mb)+" Mb";
                }
                else if(uk >= Gb && uk <Tb)
                {
                    ukuranString = String.valueOf(uk/Gb)+" Gb";
                }

                adapterDetailTugasPage.ukuranFile.setText(ukuranString);

                adapterDetailTugasPage.ivDownloadFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = setGetDetailTugasPage.getUrl();
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle(setGetDetailTugasPage.getNamafile());
                        request.setDescription("Downloading "+setGetDetailTugasPage.getNamafile());
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,setGetDetailTugasPage.getNamafile());

                        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    }
                });
            }

            @NonNull
            @Override
            public AdapterDetailTugasPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdapterDetailTugasPage(LayoutInflater.from(DetailTugasPage.this).inflate(R.layout.row_recyclerview_attachment_detail_tugas,parent,false));

            }
        };

        rvLampiranDetail.setAdapter(adapter);

    }
}
