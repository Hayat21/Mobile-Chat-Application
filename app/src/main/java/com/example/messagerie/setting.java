package com.example.messagerie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class setting extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    String download_url;
    private ImageButton mStatusBtn;
    private ImageButton mImageBtn;
    private ImageButton mnameBtn;

    String thumb_downloadUrl;

    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_name);
        mStatus = (TextView) findViewById(R.id.settings_status);

        mStatusBtn = (ImageButton) findViewById(R.id.settings_status_btn);
        mImageBtn = (ImageButton) findViewById(R.id.settings_image_btn);
        mnameBtn = (ImageButton) findViewById(R.id.settings_name_btn);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("username").getValue().toString();
                final String image = dataSnapshot.child("imageURL").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
               // String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.ic_account).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.ic_account).into(mDisplayImage);

                        }


                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(setting.this, Statut.class);
                status_intent.putExtra("status_value", status_value);
                startActivity(status_intent);

            }
        });
       mnameBtn.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
               String name_value = mName.getText().toString();

               Intent name_intent = new Intent(setting.this, Username.class);
               name_intent.putExtra("name_value", name_value);
               startActivity(name_intent);
           }
       });


        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECTIONNER IMAGE"), GALLERY_PICK);



                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(setting.this);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(setting.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(setting.this);
                mProgressDialog.setTitle("Téléchargement d'image...");
                mProgressDialog.setMessage("Veuillez patienter pendant que nous téléchargeons et traitons l'image\n.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                final StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");



                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url = uri.toString();

                                }
                            });
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            thumb_downloadUrl = uri.toString();

                                        }
                                    });
                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("imageURL", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(setting.this, "Téléchargement réussi.\n", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(setting.this, "Erreur lors du téléchargement de la miniature.\n.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(setting.this, "Erreur lors du téléchargement.\n", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
