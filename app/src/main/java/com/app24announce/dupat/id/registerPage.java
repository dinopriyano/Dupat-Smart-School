package com.app24announce.dupat.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class registerPage extends AppCompatActivity {

    EditText txtNama,txtNis,txtKelas,txtEmail,txtPassword;
    Button btnDaftar;
    TextView btnMasuk;
    Spinner spinKelas;
    DatabaseReference myRef;
    LazyLoader progBarReg;
    FirebaseAuth myAuth;
    RelativeLayout bgRegLoad;
    ArrayAdapter<String> kumKelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

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
        bgRegLoad.setVisibility(View.INVISIBLE);

        final String[] item = new String[1];
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtNis = (EditText) findViewById(R.id.txtNis);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPass);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnMasuk = (TextView) findViewById(R.id.btnMas);
        spinKelas = (Spinner) findViewById(R.id.spKelas);
        kumKelas = new ArrayAdapter<String>(registerPage.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.kelas));
        kumKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKelas.setAdapter(kumKelas);



        myRef = FirebaseDatabase.getInstance().getReference("user");
        myAuth = FirebaseAuth.getInstance();

        spinKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String kelas = adapterView.getItemAtPosition(i).toString();

                btnDaftar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String nama = txtNama.getText().toString();
                        final String nis = txtNis.getText().toString();
                        final String email = txtEmail.getText().toString();
                        final String password = txtPassword.getText().toString();
                        final String posisi = "murid";
                        final String nuptk = "0";
                        final String ketuakelas = "0";
                        final String fotoprofil = "0";
                        final String gurupiket = "0";







                        if(txtNama.getText().toString().equals(""))
                        {
                            txtNama.setError("Nama wajib di isi!");
                        }
//                        else if(txtNama.length() > 20 )
//                        {
//                            txtNama.setError("Nama maksimal 20 huruf! Nama harap disingkat!");
//                        }
                        else if(txtNis.getText().toString().equals(""))
                        {
                            txtNis.setError("NIS wajib di isi!");
                        }

                        else if(txtEmail.getText().toString().equals(""))
                        {
                            txtEmail.setError("Email wajib di isi!");
                        }
                        else if(txtPassword.getText().toString().equals(""))
                        {
                            txtPassword.setError("Password wajib di isi!");
                        }
                        else if(txtPassword.length() < 6)
                        {
                            txtPassword.setError("Password lebih dari 6");
                        }
                        else if(kelas.equals("Pilih Kelas"))
                        {
                            Toast.makeText(registerPage.this,"Pilih Kelas Terlebih dahulu",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            bgRegLoad.setVisibility(View.VISIBLE);
                            myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(registerPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful())
                                    {

                                        try
                                        {
                                            throw task.getException();
                                        }
                                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                        {
                                            txtEmail.setError("email tidak valid");
                                            bgRegLoad.setVisibility(View.INVISIBLE);
                                        }
                                        catch (FirebaseAuthUserCollisionException existEmail)
                                        {
                                            txtEmail.setError("email sudah ada");
                                            bgRegLoad.setVisibility(View.INVISIBLE);
                                            new SweetAlertDialog(registerPage.this,SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Kesalahan")
                                                    .setContentText("Email sudah ada")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .showCancelButton(false)
                                                    .show();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            bgRegLoad.setVisibility(View.INVISIBLE);

                                        }

                                    }
                                    else
                                    {
                                        final UserInformation userInfo = new UserInformation(nama,nis,kelas,email,password,posisi,ketuakelas,fotoprofil,nuptk,gurupiket);
                                        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("kelas").child(kelas).child("murid").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        txtNama.setText("");
                                                        txtEmail.setText("");
                                                        txtNis.setText("");
                                                        txtPassword.setText("");
                                                        spinKelas.setSelection(0);
                                                        bgRegLoad.setVisibility(View.INVISIBLE);
                                                        Snackbar.make(findViewById(R.id.registerPage),"register berhasil", Snackbar.LENGTH_LONG).setAction("Action!",null).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }


                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(registerPage.this,loginPage.class));
            }
        });


    }


}
