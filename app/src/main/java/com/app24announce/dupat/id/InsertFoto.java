package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class InsertFoto extends AppCompatActivity {

    CircleImageView gantiPP;
    CircleImageView fotoProfile;
    FirebaseAuth myAuth;
    DatabaseReference myRefHome;
    Button upPP;
    DatabaseReference myRefUserPP;
    TextView textNama;
    FirebaseDatabase database;
    private static final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    private StorageReference UserProfileImageRef;
    Uri ImagePatch,imageUriResultCrop;
    private static final String TAG = "InsertFoto";

    LazyLoader progBarReg;
    RelativeLayout bgRegLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_foto);
        gantiPP = (CircleImageView) findViewById(R.id.btnGantiPP);
        fotoProfile = (CircleImageView) findViewById(R.id.fotoProfile);
        myAuth = FirebaseAuth.getInstance();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Image Profile").child(myAuth.getUid());
        upPP = (Button) findViewById(R.id.btnUploadPP);
        database = FirebaseDatabase.getInstance();
        myRefUserPP = database.getReference("user").child(myAuth.getUid()).child("fotoprofil");
        myRefUserPP.keepSynced(true);

        myRefHome = database.getReference("user").child(myAuth.getUid());
        myRefHome.keepSynced(true);

        textNama = (TextView) findViewById(R.id.textNama);

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

        myRefHome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nama = dataSnapshot.child("nama").getValue().toString();

                textNama.setText(nama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gantiPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setAction();
                intent.setType("image/*");
                startActivityForResult(intent,CODE_IMG_GALLERY);
            }
        });

        upPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bgRegLoad.setVisibility(View.VISIBLE);

                if(imageUriResultCrop == null)
                {
                    bgRegLoad.setVisibility(View.GONE);
                    new SweetAlertDialog(InsertFoto.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Kesalahan")
                            .setContentText("Silahkan pilih foto")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .showCancelButton(false)
                            .show();
                }
                else
                {
                    UserProfileImageRef.putFile(imageUriResultCrop).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            UserProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    myRefUserPP.setValue(uri.toString()+".jpg").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            bgRegLoad.setVisibility(View.GONE);
                                            //Toast.makeText(InsertFoto.this, "berhasil menambahkan foto profil", Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(InsertFoto.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(InsertFoto.this,homePage.class);
                                            intent.putExtra("suksesTambahFoto","1");
                                            intent.putExtra("update",true);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(InsertFoto.this, "gagal menambahkan foto profile", Toast.LENGTH_SHORT).show();
                            new SweetAlertDialog(InsertFoto.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan")
                                    .setContentText("Gagal menambahkan foto")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .showCancelButton(false)
                                    .show();
                        }
                    });
                }
            }
        });


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
                //Toast.makeText(this, imageUriResultCrop.toString(), Toast.LENGTH_SHORT).show();
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
        uCrop.start(InsertFoto.this);
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
}
