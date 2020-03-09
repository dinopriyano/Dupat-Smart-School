package com.app24announce.dupat.id;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class loginPage extends AppCompatActivity {

    DatabaseReference username,pass,suser,spass;
    private SharedPreferenceConfig preferenceConfig;
    private SharedPreferenceConfig2 preferenceConfig2;
    LazyLoader progBar;
    RelativeLayout bgLogLoad;
    FirebaseUser firebaseUser;
    String user,pw,stuser,stpw;
    FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        bgLogLoad = (RelativeLayout) findViewById(R.id.bgLoadLog);
        progBar = (LazyLoader) findViewById(R.id.progresBar);

        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(150);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());

        progBar.addView(loader);
        bgLogLoad.setVisibility(View.INVISIBLE);

        TextView daftar = (TextView) findViewById(R.id.btnDaf);
        Button login = (Button) findViewById(R.id.btnLogin);
        final EditText uname = (EditText) findViewById(R.id.txtUser);
        final EditText upass = (EditText) findViewById(R.id.txtPass);
        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        preferenceConfig2 = new SharedPreferenceConfig2(getApplicationContext());

//        if(preferenceConfig.readLoginStatus())
//        {
//            startActivity(new Intent(loginPage.this,homePage.class));
//            finish();
//        }

        if(firebaseUser != null)
        {
            startActivity(new Intent(loginPage.this,homePage.class));
            finish();
        }
        else
        {
            stopService(new Intent(this, MyService.class));
        }




        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginPage.this,registerPage.class));
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uname.getText().toString().equals(""))
                {
                    uname.setError("email wajib di isi!");
                }
                else if(upass.getText().toString().equals(""))
                {
                    upass.setError("password wajib di isi!");
                }
                else
                {
                    bgLogLoad.setVisibility(View.VISIBLE);
                    myAuth.signInWithEmailAndPassword(uname.getText().toString(),upass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //startService(new Intent(loginPage.this,MyService.class));
                                startActivity(new Intent(loginPage.this,homePage.class));
                                preferenceConfig.writeLoginStatus(true);
                                bgLogLoad.setVisibility(View.INVISIBLE);
                                finish();
                            }
                            else
                            {
                                bgLogLoad.setVisibility(View.INVISIBLE);
                                new SweetAlertDialog(loginPage.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Kesalahan")
                                        .setContentText("akun tidak cocok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                        .showCancelButton(false)
                                        .show();
                            }
                        }
                    });

//                    if(uname.getText().toString().equals(user) && upass.getText().toString().equals(pw))
//                    {
//                        startActivity(new Intent(loginPage.this,homePage.class));
//                        preferenceConfig.writeLoginStatus(true);
//                        finish();
//                    }
//                    else if(uname.getText().toString().equals(stuser) && upass.getText().toString().equals(stpw))
//                    {
//                        startActivity(new Intent(loginPage.this,StudentHome.class));
//                        preferenceConfig2.writeLoginStatus2(true);
//                        finish();
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Username atau Password anda salah!",Toast.LENGTH_LONG).show();
//                    }
                }

            }
        });
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
