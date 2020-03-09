package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.TransitionManager;
import androidx.transition.Transition;
import androidx.transition.Fade;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class actTambahBlog extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout rvChangePhoto,rvTapTo;
    private static final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    Uri ImagePatch,imageUriResultCrop;
    ImageView ppBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_blog);

        toolbar = (Toolbar) findViewById(R.id.tbTambahBlog);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvChangePhoto = (RelativeLayout) findViewById(R.id.rvChangePhoto);
        rvTapTo = (RelativeLayout) findViewById(R.id.rvTapTo);
        ppBlog = (ImageView) findViewById(R.id.ppBlog);

        rvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transition transition = new Fade();
                transition.setDuration(600);
                transition.addTarget(R.id.rvTapTo);

                TransitionManager.beginDelayedTransition(rvChangePhoto, transition);
                rvTapTo.setVisibility(View.GONE);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setAction();
                intent.setType("image/*");
                startActivityForResult(intent,CODE_IMG_GALLERY);
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
        else if(requestCode==CODE_IMG_GALLERY && resultCode == RESULT_CANCELED)
        {
            Transition transition = new Fade();
            transition.setDuration(600);
            transition.addTarget(R.id.rvTapTo);

            TransitionManager.beginDelayedTransition(rvChangePhoto, transition);
            rvTapTo.setVisibility(View.VISIBLE);
        }
        else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK)
        {
            imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop != null)
            {
                ppBlog.setImageURI(null);
                ppBlog.setImageURI(imageUriResultCrop);
                //Toast.makeText(this, imageUriResultCrop.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  void startCrop(@NonNull Uri uri)
    {
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(1,1);
        uCrop.withMaxResultSize(700,400);
        uCrop.withOptions(getCropOptions());
        uCrop.start(actTambahBlog.this);
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
