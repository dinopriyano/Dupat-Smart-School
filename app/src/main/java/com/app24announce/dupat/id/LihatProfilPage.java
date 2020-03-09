package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LihatProfilPage extends AppCompatActivity {


    Toolbar toolbar;

    FirebaseAuth myAuth;
    DatabaseReference myRefUserPP,myRefMurid;


    RelativeLayout formKelas;

    String potSis,posSis,namaSis,kelSis,nisSis,nuptkGur,emailSis,passSis,guruPik,ketuaKel;

    CircleImageView fotoProfile,btnGantiPP;
    TextView namaSiswa,nisSiswa,kelasSiswa,txtJudulNis;
    ImageView editNama,editNis,editKelas;
    Spinner spKelasProfil;

    Animation fromsmall;
    private StorageReference UserProfileImageRef;


    private static final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    Uri ImagePatch,imageUriResultCrop;
    private static final String TAG = "InsertFoto";

    TextView judulUbah;
    Button btnTutupPopup,btnSelesai,btnSimpan;
    EditText textUbahValue;

    RelativeLayout bgGelap;
    LinearLayout isiEditValue;

    LazyLoader progBarReg;
    RelativeLayout bgRegLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profil_page);

        bgRegLoad = (RelativeLayout) findViewById(R.id.bgLoadReg);
        progBarReg = (LazyLoader) findViewById(R.id.progresBarReg);

        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(150);
        loader.setFirstDelayDuration(150);
        loader.setSecondDelayDuration(150);
        loader.setInterpolator(new LinearInterpolator());

        progBarReg.addView(loader);
        bgRegLoad.setVisibility(View.GONE);

        posSis = getIntent().getStringExtra("posSis");
        kelSis = getIntent().getStringExtra("kelSis");
        nisSis = getIntent().getStringExtra("nisSis");
        namaSis = getIntent().getStringExtra("namaSis");
        potSis = getIntent().getStringExtra("potSis");
        nuptkGur = getIntent().getStringExtra("nuptkGur");
        emailSis = getIntent().getStringExtra("emailSis");
        passSis = getIntent().getStringExtra("passSis");
        guruPik = getIntent().getStringExtra("guruPik");
        ketuaKel = getIntent().getStringExtra("ketuaKel");

        fotoProfile = (CircleImageView) findViewById(R.id.fotoProfile);
        btnGantiPP = (CircleImageView) findViewById(R.id.btnGantiPP);
        namaSiswa = (TextView) findViewById(R.id.namaSiswa);
        nisSiswa = (TextView) findViewById(R.id.nisSiswa);
        kelasSiswa = (TextView) findViewById(R.id.kelasSiswa);
        formKelas = (RelativeLayout) findViewById(R.id.formKelas);
        txtJudulNis = (TextView) findViewById(R.id.txtJudulNis);
        spKelasProfil = (Spinner) findViewById(R.id.spKelasProfil);

        bgGelap = (RelativeLayout) findViewById(R.id.bgGelap);
        isiEditValue = (LinearLayout) findViewById(R.id.isiEditValue);
        textUbahValue = (EditText) findViewById(R.id.textUbahValue);

        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall);

        myAuth = FirebaseAuth.getInstance();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Image Profile").child(myAuth.getUid());
        myRefUserPP = FirebaseDatabase.getInstance().getReference("user").child(myAuth.getUid());
        myRefUserPP.keepSynced(true);
        myRefMurid = FirebaseDatabase.getInstance().getReference("kelas");
        myRefMurid.keepSynced(true);

        editNama = (ImageView) findViewById(R.id.editNama);
        editNis = (ImageView) findViewById(R.id.editNis);
        editKelas = (ImageView) findViewById(R.id.editKelas);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        judulUbah = (TextView) findViewById(R.id.judulUbah);
        btnTutupPopup = (Button) findViewById(R.id.btnTutupPopup);
        btnSelesai = (Button) findViewById(R.id.btnSelesai);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        //isiEditValue.setAlpha(0);
        //spKelasProfil.setVisibility(View.GONE);


        Picasso.get().load(potSis).placeholder(R.drawable.student).error(R.drawable.student).into(fotoProfile);

        if(posSis.equals("guru"))
        {
            formKelas.setVisibility(View.GONE);
            txtJudulNis.setText("NUPTK");
            nisSiswa.setText(nuptkGur);
            namaSiswa.setText(namaSis);

        }
        else
        {
            formKelas.setVisibility(View.VISIBLE);
            txtJudulNis.setText("NIS");
            nisSiswa.setText(nisSis);
            namaSiswa.setText(namaSis);
            kelasSiswa.setText(kelSis);
        }


        btnGantiPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setAction();
                intent.setType("image/*");
                startActivityForResult(intent,CODE_IMG_GALLERY);
            }
        });

        editNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textUbahValue.setVisibility(View.VISIBLE);
                spKelasProfil.setVisibility(View.GONE);
                bgGelap.setVisibility(View.VISIBLE);
                isiEditValue.setVisibility(View.VISIBLE);
                isiEditValue.setAlpha(1);
                isiEditValue.startAnimation(fromsmall);
                judulUbah.setText("Ubah Nama");
                textUbahValue.setText(namaSiswa.getText().toString());

                btnSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        namaSiswa.setText(textUbahValue.getText().toString());
                        bgGelap.setVisibility(View.GONE);
                        isiEditValue.setVisibility(View.GONE);
                        isiEditValue.setAlpha(0);
                    }
                });
            }
        });

        editNis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spKelasProfil.setVisibility(View.GONE);
                bgGelap.setVisibility(View.VISIBLE);
                textUbahValue.setVisibility(View.VISIBLE);
                isiEditValue.setVisibility(View.VISIBLE);
                isiEditValue.setAlpha(1);
                isiEditValue.startAnimation(fromsmall);
                if(posSis.equals("guru"))
                {
                    judulUbah.setText("Ubah NUPTK");
                }
                else
                {
                    judulUbah.setText("Ubah NIS");
                }

                textUbahValue.setText(nisSiswa.getText().toString());

                btnSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nisSiswa.setText(textUbahValue.getText().toString());
                        bgGelap.setVisibility(View.GONE);
                        isiEditValue.setVisibility(View.GONE);
                        isiEditValue.setAlpha(0);
                    }
                });

            }
        });

        editKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spKelasProfil.setVisibility(View.VISIBLE);
                textUbahValue.setVisibility(View.GONE);
                bgGelap.setVisibility(View.VISIBLE);
                isiEditValue.setVisibility(View.VISIBLE);
                isiEditValue.setAlpha(1);
                judulUbah.setText("Ubah Kelas");
                isiEditValue.startAnimation(fromsmall);
                String[] kel = getResources().getStringArray(R.array.kelas);
                spKelasProfil.setSelection(Arrays.asList(kel).indexOf(kelasSiswa.getText().toString()));

                spKelasProfil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final String kelas = adapterView.getItemAtPosition(i).toString();
                        btnSelesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                kelasSiswa.setText(kelas);
                                bgGelap.setVisibility(View.GONE);
                                isiEditValue.setVisibility(View.GONE);
                                isiEditValue.setAlpha(0);

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });

        btnTutupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgGelap.setVisibility(View.GONE);
                isiEditValue.setVisibility(View.GONE);
                isiEditValue.setAlpha(0);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(posSis.equals("guru"))
                {
                    simpanData(namaSiswa.getText().toString(),"0","0",nisSiswa.getText().toString());
                }
                else
                {
                    simpanData(namaSiswa.getText().toString(),kelasSiswa.getText().toString(),nisSiswa.getText().toString(),"0");
                }
            }
        });





    }

    public void simpanData(final String nama,final String kelas,final String nis, final String nuptk)
    {
        bgRegLoad.setVisibility(View.VISIBLE);
        if(imageUriResultCrop == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("nama",nama);
            map.put("nis",nis);
            map.put("kelas",kelas);
            map.put("nuptk",nuptk);

            myRefUserPP.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    final Map<String,Object> map2 = new HashMap<>();
                    map2.put("nama",nama);
                    map2.put("nis",nis);
                    map2.put("kelas",kelas);
                    map2.put("nuptk",nuptk);
                    map2.put("email",emailSis);
                    map2.put("password",passSis);
                    map2.put("posisi",posSis);
                    map2.put("fotoprofil",potSis);
                    map2.put("gurupiket",guruPik);
                    map2.put("ketuakelas",ketuaKel);

                    myRefMurid.child(kelSis).child("murid").child(myAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            myRefMurid.child(kelas).child("murid").child(myAuth.getUid()).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    bgRegLoad.setVisibility(View.GONE);
                                    Snackbar.make(findViewById(R.id.lihatProfilPage),"berhasil mengubah profil", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                                }
                            });
                        }
                    });


                }
            });
        }
        else
        {
            UserProfileImageRef.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(final Uri uri) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("fotoprofil",uri.toString()+".jpg");
                            map.put("nama",nama);
                            map.put("nis",nis);
                            map.put("kelas",kelas);
                            map.put("nuptk",nuptk);

                            myRefUserPP.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    final Map<String,Object> map2 = new HashMap<>();
                                    map2.put("nama",nama);
                                    map2.put("nis",nis);
                                    map2.put("kelas",kelas);
                                    map2.put("nuptk",nuptk);
                                    map2.put("email",emailSis);
                                    map2.put("password",passSis);
                                    map2.put("posisi",posSis);
                                    map2.put("fotoprofil",uri.toString()+".jpg");
                                    map2.put("gurupiket",guruPik);
                                    map2.put("ketuakelas",ketuaKel);

                                    myRefMurid.child(kelSis).child("murid").child(myAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            myRefMurid.child(kelas).child("murid").child(myAuth.getUid()).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    bgRegLoad.setVisibility(View.GONE);
                                                    Snackbar.make(findViewById(R.id.lihatProfilPage),"berhasil mengubah profil", Snackbar.LENGTH_LONG).setAction("Action!",null).show();

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bgRegLoad.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CODE_IMG_GALLERY && resultCode==RESULT_OK)
        {

            ImagePatch = data.getData();
            if(ImagePatch != null)
            {

                startCrop(ImagePatch);
            }

        }
        else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK)
        {
            imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop != null)
            {
                fotoProfile.setImageURI(null);
                fotoProfile.setImageURI(imageUriResultCrop);
            }
        }


    }

    private  void startCrop(@NonNull Uri uri)
    {
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(1,1);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(LihatProfilPage.this);
    }

    private  UCrop.Options getCropOptions()
    {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        return options;
    }
}
