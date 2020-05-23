package com.example.hithblogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.hithblogs.model.imagedb;

import com.example.hithblogs.util.JournalApi;
import com.example.hithblogs.util.imagemodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.PostalAddress;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostBlogActivity extends AppCompatActivity {
    private CircleImageView profileimage;
    private Uri mainImageUri=null;
    private Button savebutton;
    private ProgressBar imagsaveprogress;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        savebutton=findViewById(R.id.Save_button);
        profileimage=findViewById(R.id.profile_pic);
        imagsaveprogress=findViewById(R.id.svprogress);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username= JournalApi.getInstance().getUsername();
                final String Userid=JournalApi.getInstance().getUserId();
                imagsaveprogress.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(username)&&mainImageUri!=null){
                    StorageReference filepath=storageReference.child("profile images").child(Userid+".jpeg");
                    filepath.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String imageurl= mainImageUri.toString();
                            imagedb Imagedb=new imagedb();
                            Imagedb.setImageUrl(imageurl);
                            Imagedb.setUsername(username);
                            Imagedb.setUserId(Userid);
                            collectionReference.add(Imagedb).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                            imagsaveprogress.setVisibility(View.INVISIBLE);
                                            Intent intent=new Intent(PostBlogActivity.this,mainBlogActivity.class);
                                            intent.putExtra("imageuri",mainImageUri);
                                            startActivity(intent);
                                            finish();


                                }
                            });

                        }
                    });

                }
            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(PostBlogActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        //Toast.makeText(PostBlogActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(PostBlogActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );

                    }
                    else{
                        //Toast.makeText(PostBlogActivity.this,"Acess already Granted",Toast.LENGTH_LONG).show();
                        // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setMinCropResultSize(512,512)
                                .start(PostBlogActivity.this);



//// start cropping activity for pre-acquired image saved on the device
//                        Uri imageUri;
//                        CropImage.activity(imageUri)
//                                .start(this);
//
//// for fragment (DO NOT use `getActivity()`)
//                        CropImage.activity()
//                                .start(getContext(), this);
                    }

                }
            }
        });


}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                profileimage.setImageURI(mainImageUri);
                imagemodel modelimage =new imagemodel();
                savebutton.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
