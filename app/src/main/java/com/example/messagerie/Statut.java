package com.example.messagerie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Statut extends AppCompatActivity {




    private TextInputLayout mStatus;
    private TextView mSavebtn;
    private TextView mCancelbtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;


    //Progress
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statut);


        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

       // mToolbar = (Toolbar) findViewById(R.id.status_appBar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Account Status");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String status_value = getIntent().getStringExtra("status_value");

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSavebtn = (TextView) findViewById(R.id.status_save_btn);
        mCancelbtn= (TextView) findViewById(R.id.status_annuler_btn);
        mStatus.getEditText().setText(status_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(Statut.this);
                mProgress.setTitle("Enregistrer les modifications");
                mProgress.setMessage("Veuillez patienter pendant que nous enregistrons les modifications");
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();

                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgress.dismiss();
                            startActivity(new Intent(getApplicationContext() ,setting.class));

                        } else {

                            Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de l'enregistrement des modifications\n.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });
        mCancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() ,setting.class));

            }
        });


}}
