package org.camachoyury.bonappetit.mvp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.camachoyury.bonappetit.R;

import java.io.File;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    public final static String TAG = "PostActivity";
    private ImageView pic;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private EditText foodName;
    private EditText restaurant;
    private Uri savedFodUrl;
    private Button doPost;
    private Uri uriFoodPhoto;
    static final String PROVIDER_URI = "org.camachoyury.bonappetit.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();

        pic = (ImageView) findViewById(R.id.pic);
        foodName = (EditText) findViewById(R.id.edit_food);
        restaurant = (EditText) findViewById(R.id.edit_restaurant);
        doPost = (Button) findViewById(R.id.post);
        doPost.setOnClickListener(this);
        if (intent.getStringExtra("photo_path") != null){
            String photoPath = intent.getStringExtra("photo_path");
            File file = new File(photoPath);
            pic.setImageURI(Uri.fromFile(file));
            uriFoodPhoto = FileProvider.getUriForFile(this,
                    PROVIDER_URI, file);
        }
        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }


    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        // traer la referenica las fotos/<FILENAME>.jpg
        final StorageReference photoRef = mStorageRef.child("food_images")
                .child(fileUri.getLastPathSegment());

//        showProgressDialog();

        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());

        //subir la imagen
        photoRef.putFile(fileUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // obtener la URL de referencia
                        savedFodUrl= taskSnapshot.getDownloadUrl();
                        //Guardar el objeto Foodd
                        saveData(savedFodUrl);

//                        hideProgressDialog();


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {


                        Toast.makeText(PostActivity.this, "Error: upload failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void saveData(Uri savedFodUrl) {

        try {
            Food food = new Food();
            food.setUrlImage(savedFodUrl.toString());
            food.setFoodName(foodName.getText().toString().trim());
            food.setRestaurant(restaurant.getText().toString().trim());
            food.setUser(FirebaseAuth.getInstance().getCurrentUser());
            mDatabase.child("food").push().setValue(food);

        }catch (Exception e){
            e.printStackTrace();
        }


        Intent intent =new Intent(this, PostActivity.class);
        startActivity(intent);


    }

    @Override
    public void onClick(View v) {
        if (v == doPost && uriFoodPhoto != null)

            uploadFromUri(uriFoodPhoto);
        }
}
