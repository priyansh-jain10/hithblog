package com.example.hithblogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hithblogs.model.BlogPost;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Home_fragment extends Fragment {
    private RecyclerView blog_list_view;
    private List<BlogPost>blog_list;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context=container.getContext();
        View v= inflater.inflate(R.layout.home_fragment,container,false);
        ImageView addbtn=v.findViewById(R.id.addbutton);
        blog_list_view=v.findViewById(R.id.Blog_recycler_view);
        blog_list=new ArrayList<>();
        blogRecyclerAdapter=new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference=firebaseFirestore.collection("Posts");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType()==DocumentChange.Type.ADDED){
                         BlogPost blogPost=doc.getDocument().toObject(BlogPost.class);
                         blog_list.add(blogPost);
                         blogRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             getFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddBlogFragment()).commit();
            }
        });
        return v;
    }

}


