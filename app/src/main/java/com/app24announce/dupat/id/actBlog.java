package com.app24announce.dupat.id;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class actBlog extends AppCompatActivity {

    String posisiUser;
    FloatingActionMenu btnAddBlog;
    Toolbar toolbar;
    private static final String TAG = "actBlog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        posisiUser = getIntent().getStringExtra("posisiNya");

        btnAddBlog = (FloatingActionMenu) findViewById(R.id.btnTambahBlog);
        toolbar = (Toolbar) findViewById(R.id.tbBlog);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(posisiUser.equalsIgnoreCase("guru"))
        {
            btnAddBlog.setVisibility(View.VISIBLE);
        }
        else
        {
            btnAddBlog.setVisibility(View.GONE);
        }

        Log.d(TAG, "posisi gua: "+posisiUser);
    }
}
