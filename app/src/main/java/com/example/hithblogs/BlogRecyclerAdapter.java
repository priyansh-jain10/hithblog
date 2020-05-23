package com.example.hithblogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hithblogs.model.BlogPost;

import java.util.ArrayList;
import java.util.List;


public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {


    public List<BlogPost> blog_list;
    public Context context;
    public BlogRecyclerAdapter(List<BlogPost> bloglist){
    this.blog_list=bloglist;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String desc_data=blog_list.get(position).getDescription();
            holder.setDescText(desc_data);
            String image_url=blog_list.get(position).getImageurl();
            holder.setBlogImage(image_url);

    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView description_text;
        private View mview;
        private ImageView blog_view_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setDescText(String descText){
            description_text=mview.findViewById(R.id.blog_view_text);

            description_text.setText(descText);
        }
        public void setBlogImage(String downloadurl){
            blog_view_image=mview.findViewById(R.id.blog_post_image);
            Glide.with(context).load(downloadurl).into(blog_view_image);
        }
    }
}
