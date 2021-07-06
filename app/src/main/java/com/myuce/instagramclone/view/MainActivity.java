package com.myuce.instagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myuce.instagramclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG="MainActivity";
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth= FirebaseAuth.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent( MainActivity.this,FeedActivity.class );
            startActivity( intent );
            finish();
        }
    }


    public void SignInOnClick(View view){

        email=binding.mailEditT.getText().toString().trim();
        password=binding.passwordEditT.getText().toString().trim();

        if (email.equals( "" )|| password.equals( "" )){

            Toast.makeText( MainActivity.this,"Please enter email or password",Toast.LENGTH_LONG ).show();



        }
        else{

            auth.signInWithEmailAndPassword( email,password ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(MainActivity.this,FeedActivity.class);
                    startActivity( intent );
                    finish();

                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }
            } );
        }




    }
    public void SignUpOnClick(View view){

        email= binding.mailEditT.getText().toString().trim();
        password= binding.passwordEditT.getText().toString().trim();
        if (email.equals( " " )|| password.equals( " " )){

            Toast.makeText( MainActivity.this,"Please enter email or password",Toast.LENGTH_LONG ).show();
        }

        auth.createUserWithEmailAndPassword( email,password ).addOnSuccessListener( new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent=new Intent( MainActivity.this,FeedActivity.class );
                startActivity( intent );
                finish();

            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG ).show();

            }
        } );

    }
}