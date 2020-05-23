package com.example.hithblogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hithblogs.util.JournalApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("User");

    private EditText emailEdittext;
    private EditText passwordEdittext;
    private ProgressBar progressBar;
    private Button createaccountbtn;
    private EditText usernameedittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebaseAuth = FirebaseAuth.getInstance();
        createaccountbtn = findViewById(R.id.create_acc_btn);
        progressBar = findViewById(R.id.create_ac_progress);
        usernameedittext = findViewById(R.id.username_newac_id);
        emailEdittext = findViewById(R.id.email_newac_id);
        passwordEdittext = findViewById(R.id.password_newac_details);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser = firebaseAuth.getCurrentUser();
                if (currentuser != null) {
                    // user is already logged in


                } else {

                }
            }
        };
        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEdittext.getText().toString()) &&
                        !TextUtils.isEmpty(passwordEdittext.getText().toString())
                        && !TextUtils.isEmpty(usernameedittext.getText().toString())) {
                    String email = emailEdittext.getText().toString().trim();
                    String password = passwordEdittext.getText().toString().trim();
                    String username = usernameedittext.getText().toString().trim();
                    CreateUserEmailAccount(email, password, username);
                } else {
                    Toast.makeText(CreateAccount.this, "Empty fields Not Allowed", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    private void CreateUserEmailAccount(String email, String password,final String username){
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //we take user to Add Journal Activity
                        Toast.makeText(CreateAccount.this, "task is successful", Toast.LENGTH_LONG).show();
                        currentuser = firebaseAuth.getCurrentUser();
                        assert currentuser != null;
                        final String currentUserId = currentuser.getUid();
                        //create a user map
                        Map<String, String> userObj = new HashMap<>();
                        userObj.put("username", username);
                        userObj.put("userId", currentUserId);

                        //Save to our firestore database
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("CreateAc", "onSuccess: Sucess");
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (Objects.requireNonNull(task.getResult()).exists()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String name = task.getResult().getString("username");
                                            JournalApi journalApi = JournalApi.getInstance();
                                            journalApi.setUserId(currentUserId);
                                            journalApi.setUsername(name);
                                            Log.d("CreateAccount", "onComplete: entered sucessfully");
                                            Intent intent = new Intent(CreateAccount.this, PostBlogActivity.class);
//                                                    intent.putExtra("username",name);
//                                                    intent.putExtra("userId",currentUserId); //Replaced by Journal Api global by
//                                                       declaring in manifest and puting data in ito
                                            // of application superclass
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    } else {
                        Toast.makeText(CreateAccount.this, "task is unsuccessful", Toast.LENGTH_LONG).show();
                                                Log.w("createAccont", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(CreateAccount.this, "Authentication failed.",
                                                        Toast.LENGTH_LONG).show();
                                                try {
                                                    throw task.getException();
                                                }
                                                catch (FirebaseAuthInvalidCredentialsException e) {
                                                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                                                }
                                                catch (FirebaseAuthEmailException e){
                                                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                                                }
                                                catch (FirebaseAuthException e){
                                                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else {


                    }
                }





    @Override
    protected void onStart() {
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        super.onStart();
    }}
