package com.app24announce.dupat.id;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPreferenceConfig2 {
    private SharedPreferences sharedPreferences2;
    private Context context2;

    public SharedPreferenceConfig2(Context context2)
    {
        this.context2 = context2;
        sharedPreferences2 = context2.getSharedPreferences(context2.getResources().getString(R.string.login_preference2),Context.MODE_PRIVATE);

    }


    public void writeLoginStatus2(boolean status2)
    {
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean(context2.getResources().getString(R.string.login_status_preference2),status2);
        editor.commit();
    }

    public boolean readLoginStatus2()
    {
        boolean status2 = false;
        status2 = sharedPreferences2.getBoolean(context2.getResources().getString(R.string.login_status_preference2),false);
        return status2;
    }
}


