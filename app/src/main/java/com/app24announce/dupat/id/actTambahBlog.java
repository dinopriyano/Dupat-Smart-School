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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ebolo.krichtexteditor.fragments.KRichEditorFragment;
import com.ebolo.krichtexteditor.fragments.Options;
import com.ebolo.krichtexteditor.ui.widgets.EditorButton;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Arrays;

import io.paperdb.Paper;

public class actTambahBlog extends AppCompatActivity implements View.OnTouchListener {

    Toolbar toolbar;
    private KRichEditorFragment editorFragment;
    RelativeLayout rvChangePhoto,rvTapTo;
    private static final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    Uri ImagePatch,imageUriResultCrop;
    ImageView ppBlog;

    private static final String TAG = "actTambahBlog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_blog);

        toolbar = (Toolbar) findViewById(R.id.tbTambahBlog);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editorFragment = (KRichEditorFragment) getSupportFragmentManager().findFragmentByTag("EDITOR");

        rvChangePhoto = (RelativeLayout) findViewById(R.id.rvChangePhoto);
        rvTapTo = (RelativeLayout) findViewById(R.id.rvTapTo);
        ppBlog = (ImageView) findViewById(R.id.ppBlog);

        if (editorFragment == null)
            editorFragment = KRichEditorFragment.getInstance(
                    new Options()
                            .placeHolder("Tulis konten disini...")
                            .onImageButtonClicked(new Runnable() {
                                @Override
                                public void run() {
                                    ImagePicker.create(actTambahBlog.this).start();
                                }
                            })
                            // Un-comment this line and comment out the layout below to
                            // disable the toolbar
                            // .showToolbar(false)
                            .buttonLayout( Arrays.asList(
                                    EditorButton.UNDO,
                                    EditorButton.REDO,
                                    EditorButton.IMAGE,
                                    EditorButton.LINK,
                                    EditorButton.BOLD,
                                    EditorButton.ITALIC,
                                    EditorButton.UNDERLINE,
                                    EditorButton.SUBSCRIPT,
                                    EditorButton.SUPERSCRIPT,
                                    EditorButton.STRIKETHROUGH,
                                    EditorButton.JUSTIFY_LEFT,
                                    EditorButton.JUSTIFY_CENTER,
                                    EditorButton.JUSTIFY_RIGHT,
                                    EditorButton.JUSTIFY_FULL,
                                    EditorButton.ORDERED,
                                    EditorButton.UNORDERED,
                                    EditorButton.CHECK,
                                    EditorButton.NORMAL,
                                    EditorButton.H1,
                                    EditorButton.H2,
                                    EditorButton.H3,
                                    EditorButton.H4,
                                    EditorButton.H5,
                                    EditorButton.H6,
                                    EditorButton.INDENT,
                                    EditorButton.OUTDENT,
                                    EditorButton.BLOCK_QUOTE,
                                    EditorButton.BLOCK_CODE
                            ) )
                            .onInitialized(new Runnable() {
                                @Override
                                public void run() {
                                    // Simulate loading saved contents action
                                    editorFragment.getEditor().setContents(
                                            Paper.book("demo").read("content", "")
                                    );
                                }
                            })
            );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, editorFragment, "EDITOR")
                .commit();

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

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                // The second param (true/false) would not reflect BASE64 mode or not
                // Normal URL mode would pass the URL
                /*editorFragment.getEditor().command(EditorButton.IMAGE, false, "https://" +
                        "beebom-redkapmedia.netdna-ssl.com/wp-content/uploads/2016/01/" +
                        "Reverse-Image-Search-Engines-Apps-And-Its-Uses-2016.jpg");*/

                // For BASE64, image file path would be passed instead
                Log.d(TAG, "lokasi image: "+image.getPath());
                editorFragment.getEditor().command(EditorButton.IMAGE, true, image.getPath());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getTag()== "EDITOR")
        {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            switch (motionEvent.getAction()&MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_UP:
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    Log.d(TAG, "touchhhhh");
                    break;
            }
        }

        return false;
    }
}
