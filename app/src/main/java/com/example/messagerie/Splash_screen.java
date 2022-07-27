package com.example.messagerie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_screen extends Activity {
private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              //  Intent i=new Intent(Splash_screen.this,Start.class);
                //startActivity(i);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext() ,Start.class));
                    finish();
                }

            }
        },SPLASH_TIME_OUT);
    }

    }

