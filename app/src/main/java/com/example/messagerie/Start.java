package com.example.messagerie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Start extends AppCompatActivity {
    Button login, register;
    private long backpressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext() ,login.class));
                    finish();
                }
              //  startActivity(new Intent(Start.this,login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext() ,inscrire.class));
                    finish();
                }
              //  startActivity(new Intent(Start.this, inscrire.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(backpressedTime+2000>System.currentTimeMillis()){
            //super.onBackPressed();
            //return;
            finishAffinity();

        }
        else{


            Toast.makeText(getBaseContext(),"appuyez à nouveau pour quitter HeyApp",Toast.LENGTH_SHORT).show();
        }
        backpressedTime=System.currentTimeMillis();    }
}
