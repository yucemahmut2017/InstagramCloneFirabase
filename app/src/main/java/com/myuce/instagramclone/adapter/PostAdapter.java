package com.myuce.instagramclone.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myuce.instagramclone.databinding.RecyclerviewRowBinding;
import com.myuce.instagramclone.model.PostM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
   private ArrayList<PostM> postMArrayList;


   public PostAdapter(ArrayList<PostM> postMArrayList){

       this.postMArrayList=postMArrayList;
   }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent ,int viewType) {
        RecyclerviewRowBinding recyclerviewRowBinding=RecyclerviewRowBinding.inflate( LayoutInflater.from( parent.getContext() ),parent,false);
        return new PostHolder( recyclerviewRowBinding );
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder ,int position) {

        holder.recyclerviewRowBinding.recyclerViewUserMailT.setText( postMArrayList.get( position ).getEmail() );
        holder.recyclerviewRowBinding.recyclerViewUserCommentT.setText( postMArrayList.get( position ).getComment() );
        Picasso.get().load( postMArrayList.get( position ).getDownloandUrl() ).into( holder.recyclerviewRowBinding.recyclerviewImageView);

    }

    @Override
    public int getItemCount() {
        return postMArrayList.size();
    }

    class  PostHolder extends  RecyclerView.ViewHolder{

        RecyclerviewRowBinding recyclerviewRowBinding;

        public PostHolder(RecyclerviewRowBinding recyclerviewRowBinding) {
            super( recyclerviewRowBinding.getRoot() );
            this.recyclerviewRowBinding=recyclerviewRowBinding;


        }
    }
}
