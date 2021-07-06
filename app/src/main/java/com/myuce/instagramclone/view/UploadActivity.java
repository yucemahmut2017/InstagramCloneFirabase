package com.myuce.instagramclone.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myuce.instagramclone.databinding.ActivityUploadBinding;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {


    //firebase
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;


    private  FirebaseAuth firebaseAuth;

    Uri imageData;
    private ActivityUploadBinding binding;
   // Bitmap selectedBitmap;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activityLauncher();

        firebaseStorage=FirebaseStorage.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=firebaseStorage.getReference();

    }
    public void SelectImage(View view){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){



            //neden izni istiyorsun açıkla <TR>
            //Why you ara want this permision
            if(ActivityCompat.shouldShowRequestPermissionRationale( this,Manifest.permission.READ_EXTERNAL_STORAGE )){

                //LENGTH_INDEFINITE for indefinite period
                Snackbar.make(view, "Permission for need gallery", Snackbar.LENGTH_INDEFINITE).setAction( "Give permission" ,new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ask permission

                        permissionLauncher.launch( Manifest.permission.READ_EXTERNAL_STORAGE );
                    }
                } ).show();

            }else{
                //ask permission but not info
                permissionLauncher.launch( Manifest.permission.READ_EXTERNAL_STORAGE );
            }

        }else{

            Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            activityResultLauncher.launch( intentToGallery );
        }

    }
    public void UploadClick(View view){

        if(imageData!=null){


            //universal uniq id
            UUID uuid=UUID.randomUUID();
            String imageName="images/"+uuid+".jpg";
            storageReference.child( imageName ).putFile( imageData ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //downloand url
                    StorageReference newReferences=firebaseStorage.getReference(imageName);

                    newReferences.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloandURL=uri.toString();
                            String userComment=binding.commentEditT.getText().toString();
                            FirebaseUser currentUser=firebaseAuth.getCurrentUser();
                           String userEmail=currentUser.getEmail();

                            HashMap<String,Object> postData=new HashMap<>();

                            postData.put( "userMail",userEmail );
                            postData.put( "downloandURL",downloandURL );
                            postData.put( "userComment",userComment );
                            postData.put( "date",FieldValue.serverTimestamp() );

                           firebaseFirestore.collection( "Posts" ).add( postData ).addOnSuccessListener( new OnSuccessListener<DocumentReference>() {
                               @Override
                               public void onSuccess(DocumentReference documentReference) {
                                   Intent intent=new Intent( UploadActivity.this,FeedActivity.class );
                                   intent.addFlags( intent.FLAG_ACTIVITY_CLEAR_TOP );//All other activity close
                                   startActivity( intent );
                               }
                           } ).addOnFailureListener( new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {


                               }
                           } );

                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText( UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                        }
                    } );




                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG ).show();

                }
            } );


        }


    }

    private  void activityLauncher(){
        activityResultLauncher=registerForActivityResult( new ActivityResultContracts.StartActivityForResult() ,new ActivityResultCallback<ActivityResult>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode()==RESULT_OK){
                    Intent intentFromResult=result.getData();
                    if(intentFromResult!=null)
                    {

                        imageData=intentFromResult.getData();
                        binding.selectImage.setImageURI( imageData );


                        //imageData convert to Bitmap
                      /*  try {
                            if(Build.VERSION.SDK_INT>=17){
                                ImageDecoder.Source source=ImageDecoder.createSource( UploadActivity.this.getContentResolver(),imageData );
                              selectedBitmap=ImageDecoder.decodeBitmap( source );
                                binding.selectImage.setImageBitmap( selectedBitmap );
                            }
                            else{

                                selectedBitmap=MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(),imageData  );
                                binding.selectImage.setImageBitmap( selectedBitmap );
                            }

                        }catch (Exception e){
                            e.printStackTrace();

                        }*/
                    }
                }

            }
        } );

        permissionLauncher=registerForActivityResult( new ActivityResultContracts.RequestPermission() ,new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {


                if(result)
                {
                    Intent intentToGallery=new Intent( Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    activityResultLauncher.launch( intentToGallery );

                }else{
                    Toast.makeText( UploadActivity.this,"Permission needed",Toast.LENGTH_LONG ).show();
                }

            }
        } );

    }
}