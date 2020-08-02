//EditGuestLectures.java(1st version)


package com.vaagdevi.newsneventsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditGuestLectures extends AppCompatActivity {

    CircleImageView Edit_GuestLecures_profilepic;
    FloatingActionButton Edit_GuestLectures_EditImage;
    EditText Edit_GuestLectures_Name, Edit_GuestLectures_Email, Edit_GuestLectures_Date, Edit_GuestLectures_Time, Edit_GuestLectures_Desc;
    String NameStr, EmailStr, DateStr, TimeStr, DescStr;
    Button Edit_GuestLectures_Update;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private final static int GalleryPick = 1;
    String currentId, anotherGLpost;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guest_lectures);

        Edit_GuestLecures_profilepic = (CircleImageView) findViewById(R.id.edit_guestlectures_profilepicIV);
        Edit_GuestLectures_EditImage = (FloatingActionButton) findViewById(R.id.edit_guestlectures_editimage);
        Edit_GuestLectures_Name = (EditText) findViewById(R.id.edit_guestlectures_nameTV);
        Edit_GuestLectures_Email = (EditText) findViewById(R.id.edit_guestlectures_emailTV);
        Edit_GuestLectures_Date = (EditText) findViewById(R.id.edit_guestlectures_dateTV);
        Edit_GuestLectures_Time = (EditText) findViewById(R.id.edit_guestlectures_timeTV);
        Edit_GuestLectures_Desc = (EditText) findViewById(R.id.edit_guestlectures_descTV);
        Edit_GuestLectures_Update = (Button) findViewById(R.id.BTNguestlectures_update);

        progressBar = findViewById(R.id.progress_GL_post);
        progressDialog = new ProgressDialog(EditGuestLectures.this);
        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        anotherGLpost = intent.getStringExtra("Guest Lectures");
        databaseReference = FirebaseDatabase.getInstance().getReference("Guest Lectures").child(currentId);
        storageReference = FirebaseStorage.getInstance().getReference("Guest Lectures").child(currentId + ".jpg");


        Edit_GuestLectures_EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent setImage = new Intent();
                setImage.setAction(Intent.ACTION_GET_CONTENT);
                setImage.setType("image/*");
                startActivityForResult(setImage, GalleryPick);

            }
        });

        Edit_GuestLectures_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NameStr = Edit_GuestLectures_Name.getText().toString();
                EmailStr = Edit_GuestLectures_Email.getText().toString();
                DateStr = Edit_GuestLectures_Date.getText().toString();
                TimeStr = Edit_GuestLectures_Time.getText().toString();
                DescStr = Edit_GuestLectures_Desc.getText().toString();

                Update_GuestLectures();

            }
        });
    }

    public void Update_GuestLectures() {
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");
        checkConnection();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", NameStr);
        hashMap.put("email", EmailStr);
        hashMap.put("date", DateStr);
        hashMap.put("time", TimeStr);
        hashMap.put("description", DescStr);

        databaseReference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(EditGuestLectures.this, GuestLectures.class));
                            overridePendingTransition(0, 0);
                            finish();
                            Toast.makeText(EditGuestLectures.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EditGuestLectures.this, "Error occurred! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        progressDialog.dismiss();
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            progressDialog.dismiss();
            imageUri = data.getData();
            Picasso.get().load(imageUri).resize(Edit_GuestLecures_profilepic.getMeasuredWidth(), Edit_GuestLecures_profilepic.getMeasuredHeight()).placeholder(R.drawable.profile_image3).into(Edit_GuestLecures_profilepic);
//            Edit_GuestLectures_EditImage.setText("Change Image");

            final StorageReference filePath = storageReference;

            progressDialog.setTitle("Updating Your Profile Photo");
            progressDialog.setMessage("Please wait...");
            checkConnection();

            progressDialog.show();

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("profilepic").setValue(String.valueOf(uri));
                            progressDialog.dismiss();
                            Toast.makeText(EditGuestLectures.this, "Guest Lecture image updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                final Uri resultUri = result.getUri();

                final StorageReference filePath = storageReference;

                progressDialog.setTitle("Updating Your Profile Photo");
                progressDialog.setMessage("Please wait...");
                checkConnection();

                progressDialog.show();

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.child("profilepic").setValue(String.valueOf(uri));
                                progressDialog.dismiss();
                                Toast.makeText(EditGuestLectures.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }*/

   /* public void Update_GuestLectures(){

        final String name = Edit_GuestLectures_Name.getText().toString();
        final String email = Edit_GuestLectures_Email.getText().toString();
        final String date = Edit_GuestLectures_Date.getText().toString();
        final String time = Edit_GuestLectures_Time.getText().toString();
        final String description = Edit_GuestLectures_Desc.getText().toString();


        if (imageUri == null && name.isEmpty() && email.isEmpty() && date.isEmpty() && time.isEmpty() && description.isEmpty()) {
            Toast.makeText(EditGuestLectures.this, "Empty Fields", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            Edit_GuestLectures_Name.setError("Name is Required");
            Edit_GuestLectures_Name.requestFocus();
        } else if (email.isEmpty()) {
            Edit_GuestLectures_Email.setError("Email is Required");
            Edit_GuestLectures_Email.requestFocus();
        } else if (date.isEmpty()) {
            Edit_GuestLectures_Date.setError("Date is Required");
            Edit_GuestLectures_Date.requestFocus();
        } else if (time.isEmpty()) {
            Edit_GuestLectures_Time.setError("Time is Required");
            Edit_GuestLectures_Time.requestFocus();
        } else if (description.isEmpty()) {
            Edit_GuestLectures_Desc.setError("Description is Required");
            Edit_GuestLectures_Desc.requestFocus();
        } else if (!(imageUri == null && name.isEmpty() && email.isEmpty() && date.isEmpty() && time.isEmpty() && description.isEmpty())) {
            progressDialog.show();

            GuestLecturesRegdatabase guestLecturesRegdatabase = new GuestLecturesRegdatabase
                    (name, email, date, time, description, profilepic);

            FirebaseDatabase.getInstance().getReference(databaseReference.getKey()).child(mAuth.getCurrentUser().getUid())
                    .setValue(guestLecturesRegdatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                    startActivity(new Intent(EditGuestLectures.this, GuestLectures.class));
                    Toast.makeText(EditGuestLectures.this, "Updated Successfully !!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(EditGuestLectures.this, "Error occurred! Try again", Toast.LENGTH_SHORT).show();
        }
    }*/


    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            progressDialog.dismiss();
            Toast.makeText(EditGuestLectures.this, "Check Your Internet Connection!",
                    Toast.LENGTH_LONG).show();
        }
    }

}