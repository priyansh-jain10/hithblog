package com.example.hithblogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hithblogs.util.JournalApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static java.util.logging.Logger.global;

public class LoginCredentials extends AppCompatActivity {
    private Button createbtn;
    private Button SignInbtn;
    private EditText emailedittext;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText passwordEdittext;
    private FirebaseUser currentuser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_credentials);
        createbtn = findViewById(R.id.create_acc_btn);
        SignInbtn=findViewById(R.id.signin_button);
        emailedittext=findViewById(R.id.email_id);
        firebaseAuth=FirebaseAuth.getInstance();
        passwordEdittext=findViewById(R.id.password_details);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();
                if (currentuser != null) {
                    // user is already logged in


                }else{

                }
            }
        };
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCredentials.this,CreateAccount.class);
                startActivity(intent);
            }
        });


        SignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailedittext.getText().toString())&&
                        !TextUtils.isEmpty(passwordEdittext.getText().toString())){
                String email=emailedittext.getText().toString().trim();
                String password=passwordEdittext.getText().toString().trim();
                signinwithemailandpassword(email,password);
                }else{
                    Toast.makeText(LoginCredentials.this,"Empty fields Not Allowed",Toast.LENGTH_LONG).show();



                }
        }
        private void signinwithemailandpassword(String email, String password) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Logincredentials.java", "signInWithEmail:success");

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                assert user != null;
                                final String currentuserID = user.getUid();
                                collectionReference.whereEqualTo("userId", currentuserID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {

                                        }
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                JournalApi journalApi = JournalApi.getInstance();
                                                journalApi.setUsername(snapshot.getString("username"));
                                                journalApi.setUserId(snapshot.getString("userId"));
                                                startActivity(new Intent(LoginCredentials.this, mainBlogActivity.class));

                                            }
                                        }

                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginCredentials.this,"Sorry Login Failed",Toast.LENGTH_LONG).show();

                }
            })
        ;}
        });
    }
}



