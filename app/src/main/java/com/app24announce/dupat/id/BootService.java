package com.app24announce.dupat.id;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BootService extends BroadcastReceiver {

    FirebaseUser firebaseUser;
    FirebaseAuth myAuth;

    @Override
    public void onReceive(Context context, Intent intent) {
//        myAuth = FirebaseAuth.getInstance();
//        firebaseUser = myAuth.getCurrentUser();
//
//        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
//        {
//            Intent i = new Intent(context, MyService.class);
//            if(firebaseUser != null)
//            {
//                context.startActivity(i);
//            }
//            Toast.makeText(context, "Boot Service Run", Toast.LENGTH_SHORT).show();
//
//        }
//        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
//        {
//            boolean noConnectivity = intent.getBooleanExtra(
//                    ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
//            );
//            if(noConnectivity)
//            {
//
//            }
//            else
//            {
//                Toast.makeText(context, "Tidak ada koneksi !", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
