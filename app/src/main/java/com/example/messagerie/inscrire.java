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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class inscrire extends AppCompatActivity {
    EditText name, Email, Password;
    Button Submit;
    private long backpressedTime;

    FirebaseAuth auth; //instance of firebaseAuth
    DatabaseReference reference; //create a reference in my database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrire);

      name = findViewById(R.id.editTextF_Name);
        Email= findViewById(R.id.editTextEmail);
       Password = findViewById(R.id.editTextPassword);
         Submit = findViewById(R.id.Submit);

        auth = FirebaseAuth.getInstance();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = name.getText().toString();
                String txt_email = Email.getText().toString();
                String txt_password = Password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(inscrire.this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(inscrire.this, "\n" + "Le mot de passe doit être au moins de 6 caractères", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });



    }


    @Override
    public void onBackPressed() {
        if(backpressedTime+200>System.currentTimeMillis()){
           // super.onBackPressed();
            //return;
            startActivity(new Intent(getApplicationContext() ,Start.class));
        }
        else{
            startActivity(new Intent(getApplicationContext() ,Start.class));

            //Toast.makeText(getBaseContext(),"tapper encore pour quiter Heyapp",Toast.LENGTH_SHORT).show();
        }
        backpressedTime=System.currentTimeMillis();
    }






    private void register(final String editTextF_name,final String editTextEmail,final String editTextPassword){

        auth.createUserWithEmailAndPassword(editTextEmail, editTextPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String user_id = firebaseUser.getUid();
                           //create a reference Users in database
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
                          //String device_token = FirebaseInstanceId.getInstance().getToken();
                            HashMap<String, String> hashMap = new HashMap<>();
                           // hashMap.put("id", userid);
                            hashMap.put("username", editTextF_name);
                            hashMap.put("email", editTextEmail);
                            hashMap.put("status", "salut j'ai utilusé HeyApp");
                            hashMap.put("password", editTextPassword);
                            hashMap.put("imageURL", "default");
                            hashMap.put("thumb_image", "default");
                           // hashMap.put("device_token", device_token);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(inscrire.this, "inscription réussie ", Toast.LENGTH_SHORT).show();
                                       // Intent intent = new Intent(inscrire.this, MainActivity.class);
                                       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       // startActivity(intent);
                                        //finish();
                                        startActivity(new Intent(getApplicationContext() ,MainActivity.class));


                                    }
                                }
                            });
                        } else {
                            Toast.makeText(inscrire.this, "Vous ne pouvez pas vous inscrire avec cet e-mail ou ce mot de passe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
