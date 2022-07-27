package com.example.messagerie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.squareup.picasso.Picasso;
import com.example.messagerie.Adapter.SectionPagerAdapter;
import com.example.messagerie.model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); //instance of FirebaseAuth

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
       // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getName());
                if (user.getImage().equals("default")){
                    profile_image.setImageResource(R.drawable.ic_account);
                } else {

                    //change this
                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.get().load(user.getImage()).into(profile_image);
               //     with(getApplicationContext())
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        }
        else
        {

            mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, Start.class);
         startActivity(startIntent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.mymenu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                startActivity(new Intent(MainActivity.this, Start.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            case R.id.setting:
                Intent settingsIntent = new Intent(MainActivity.this, setting.class);
                startActivity(settingsIntent);
        }

        return true;


    }

    }
