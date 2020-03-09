package com.app24announce.dupat.id;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutDevPage extends AppCompatActivity {

    ImageButton btnBack;
    Toolbar toolbar;
    private Toolbar supportActionBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev_page);

        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //fungsi buat nampilin titik 3 more


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.layout_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_logout:
//                stopService(new Intent(this, MyService.class));
//                startActivity(new Intent(this, loginPage.class));
//                finish();
//
//
//                return true;
//            default:
//                return false;
//        }
//    }


}
