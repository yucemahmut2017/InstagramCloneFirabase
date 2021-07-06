package com.myuce.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myuce.instagramclone.R;
import com.myuce.instagramclone.adapter.PostAdapter;
import com.myuce.instagramclone.databinding.ActivityFeedBinding;
import com.myuce.instagramclone.databinding.ActivityMainBinding;
import com.myuce.instagramclone.model.PostM;


import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    private ActivityFeedBinding binding;

    PostAdapter postAdapter;

    ArrayList<PostM> postsMarrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postsMarrayList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        getData();

        binding.recyclerView.setLayoutManager( new LinearLayoutManager( this ) );

        postAdapter=new PostAdapter( postsMarrayList );
        binding.recyclerView.setAdapter( postAdapter );
    }



    //bind xml to java
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate( R.menu.option_menu,menu );
        return super.onCreateOptionsMenu( menu );
    }

    //Here we write what happenes when the menu are clicked.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.add_post){

            //Go to upload activity
            Intent intentToUpload=new Intent( FeedActivity.this,UploadActivity.class );
            startActivity( intentToUpload );
        }
        if(item.getItemId()==R.id.logout){

         //Logout
            auth.signOut();
            Intent intentToMain=new Intent( FeedActivity.this,MainActivity.class );
            startActivity( intentToMain );
            finish();
        }
        return super.onOptionsItemSelected( item );
    }
    private void getData(){

        firebaseFirestore.collection( "Posts" ).orderBy( "date",Query.Direction.DESCENDING ).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value ,@Nullable FirebaseFirestoreException error) {
                if(error!=null){

                    Toast.makeText( FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG ).show();


                }
                if(value!=null)
                {

                    for (DocumentSnapshot document:value.getDocuments()){


                        Map<String,Object> mData=document.getData();

                        // doing Casting here!!
                        String email=(String) mData.get( "userMail" );
                        String comment=(String)mData.get( "userComment" );
                        String downloandURL=(String)mData.get( "downloandURL" );

                        PostM postM=new PostM(email,comment,downloandURL  );
                        postsMarrayList.add( postM );





                    }

                    //let me know new data arrived
                  postAdapter.notifyDataSetChanged();
                }
            }
        } );

    }
}