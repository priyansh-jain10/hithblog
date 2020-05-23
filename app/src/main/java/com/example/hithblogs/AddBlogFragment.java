package com.example.hithblogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hithblogs.util.imagemodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBlogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK =-1 ;
    private ImageView blogimage;
    private EditText blog_description;
    private Button addbutton;
    private Uri blogimageuri=null;
    private ProgressBar add_blog_prgrs;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CollectionReference collectionReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBlogFragment newInstance(String param1, String param2) {
        AddBlogFragment fragment = new AddBlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context context=container.getContext();
        View view=inflater.inflate(R.layout.fragment_add_blog, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        final String User_id=firebaseAuth.getCurrentUser().getUid();
        blogimage=view.findViewById(R.id.image_blog);
        addbutton=view.findViewById(R.id.add_post_button);
        firebaseFirestore=FirebaseFirestore.getInstance();
        blog_description=view.findViewById(R.id.blog_description);
        add_blog_prgrs=view.findViewById(R.id.add_image_progress);
        collectionReference=firebaseFirestore.collection("Posts");
        storageReference= FirebaseStorage.getInstance().getReference();
        blogimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .start(context,AddBlogFragment.this);
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description=blog_description.getText().toString().trim();
                if(!TextUtils.isEmpty(description)&& blogimageuri!=null){
                    add_blog_prgrs.setVisibility(View.VISIBLE);
                    String randomname= FieldValue.serverTimestamp().toString();
                    final StorageReference filepath=storageReference.child("Post Images").child(randomname+".jpg");
                    filepath.putFile(blogimageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                String downloadurl=filepath.getDownloadUrl().toString();
                                Map<String,Object> postMap=new HashMap<>();
                                postMap.put("image url",downloadurl);
                                postMap.put("Description",description);
                                postMap.put("user_id",User_id);
                                postMap.put("TimeStamp",FieldValue.serverTimestamp());
                                collectionReference.add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context,"Post Was Added",Toast.LENGTH_LONG).show();

                                        }else {
                                            Toast.makeText(context,"Post Was not Added",Toast.LENGTH_LONG).show();
                                        }
                                        add_blog_prgrs.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }else{
                                    add_blog_prgrs.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                blogimageuri=result.getUri();
                blogimage.setImageURI(blogimageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
