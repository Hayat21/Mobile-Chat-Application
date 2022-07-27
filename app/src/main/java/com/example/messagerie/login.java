package com.example.messagerie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText Email,Password;
    Button login_btn;
    FirebaseAuth fAuth;
    private long backpressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



       // if(FirebaseAuth.getInstance().getCurrentUser()!=null){
        //    startActivity(new Intent(getApplicationContext() ,MainActivity.class));
           // finish();
       // }

        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.editTextEmail);
        Password=findViewById(R.id.editTextPassword);
        fAuth=FirebaseAuth.getInstance();
        login_btn=findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString().trim();
                String password=Password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email est requis");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Password.setError("Mot de pass est requis");
                    return;
                }
                if(password.length()<6){
                    Password.setError(" Le mot de passe doit être >= 6 caractères ");
                    return;
                }
                //athentification
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                            Toast.makeText(login.this, "connexion réussie ", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(login.this, "Erreur"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });


            }
        });



    }

    @Override
    public void onBackPressed() {
        if(backpressedTime+200>System.currentTimeMillis()){
            //super.onBackPressed();
            startActivity(new Intent(getApplicationContext() ,Start.class));
        }
        else{
            startActivity(new Intent(getApplicationContext() ,Start.class));

            //Toast.makeText(getBaseContext(),"tapper encore pour quiter Heyapp",Toast.LENGTH_SHORT).show();
        }
   backpressedTime=System.currentTimeMillis();
    }
}