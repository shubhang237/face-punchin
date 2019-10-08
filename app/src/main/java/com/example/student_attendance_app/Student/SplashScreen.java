package com.example.student_attendance_app.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.student_attendance_app.R;


public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = getApplication().getSharedPreferences("MyPref",0);
                boolean Signedin = sPref.getBoolean("signedin",false);
                if(Signedin){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}