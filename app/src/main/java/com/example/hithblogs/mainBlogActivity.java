package com.example.hithblogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hithblogs.util.JournalApi;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
//import com.squareup.picasso.Picasso;

public class mainBlogActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private String user_id;
    private NavigationView navigationView;
    private StorageReference storageReference;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private CircleImageView profileimage;
    private TextView username;
    private TextView emailId;
    private TextView accountmode;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blog);
        profileimage = findViewById(R.id.circleImageView);
        username = findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        emailId = findViewById(R.id.email_id);
        accountmode = findViewById(R.id.account_mode);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        storageReference= FirebaseStorage.getInstance().getReference();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open_drawer, R.string.navigation_close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Home_fragment()).commit();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            imageuri=bundle.getString("imageuri");

        }
        //Glide.with(mainBlogActivity.this).load(imageuri).into(profileimage);
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseFirestore.collection("User").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    //String image = task.getResult().getString("imageUrl");
//                    //Picasso.with( mainBlogActivity.this).load(image).placeholder(R.drawable.profile_pic).into(profileimage);
//                    if (task.getResult().exists()) {
//                        String name = task.getResult().getString("username");
//                        //String image = task.getResult().getString("imageUrl");
//                        //Log.d("mainblogactivity", "onComplete: " + name + "image" + image);
//                        //username.setText(name);
//                        Glide.with(mainBlogActivity.this).load(storageReference).into(profileimage);
//                        Toast.makeText(mainBlogActivity.this, "image adding in stage", Toast.LENGTH_LONG).show();
//
//                    } else {
//                        Toast.makeText(mainBlogActivity.this, "image adding not in stage", Toast.LENGTH_LONG).show();
//
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(mainBlogActivity.this, "image adding in stage failed", Toast.LENGTH_LONG).show();
//
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentuser==null){
            Intent intent=new Intent(mainBlogActivity.this,MainActivity.class);
            finish();
        }
        else{
            user_id=firebaseAuth.getCurrentUser().getUid();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Home_fragment()).commit();
                Toast.makeText(mainBlogActivity.this, "home button pressed", Toast.LENGTH_SHORT).show();
                menuItem.setChecked(true);
                break;
            case R.id.my_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new my_account_fragment()).commit();
                Toast.makeText(mainBlogActivity.this, "my account button pressed", Toast.LENGTH_SHORT).show();
                menuItem.setChecked(true);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

