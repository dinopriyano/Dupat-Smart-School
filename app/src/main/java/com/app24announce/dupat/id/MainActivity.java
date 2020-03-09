package com.app24announce.dupat.id;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                Intent i=new Intent(MainActivity.this,loginPage.class);
                startActivity(i);
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firebaseUser != null)
        {

        }
        else
        {
            //stopService(new Intent(MainActivity.this,MyService.class));
        }
    }

}
