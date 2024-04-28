package com.example.firstkit;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminPanel extends AppCompatActivity {
    private ImageView productimage;
    private EditText productname , productprice , productdescrip;
    private Button submit;
    private String savecurrentDate , savecurrentTime , productnamekey , downloadImageUrl;

    private static final int gallerypickup = 1;
    private Uri imageURI;
    private StorageReference productImageRef;
    private DatabaseReference productref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        productimage = findViewById(R.id.insert_image);
        productname = findViewById(R.id.product_name);
        productprice = findViewById(R.id.product_price);
        productdescrip = findViewById(R.id.product_details);


        productImageRef = FirebaseStorage.getInstance().getReference().child("product Images");

        productref = FirebaseDatabase.getInstance().getReference().child("products");
        submit = findViewById(R.id.submit_product);



        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateproduct();
            }
        });


    }

    private void validateproduct() {

        String name = productname.getText().toString();
        String price = productprice.getText().toString();
        String descriptiom = productdescrip.getText().toString();

        if(imageURI == null){
            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show();

        }
        else if(name.isEmpty()){
            productname.setError("Required");
        }

        else if(price.isEmpty()){
            productprice.setError("Required");
        }
        else if(descriptiom.isEmpty()){
            productdescrip.setError("Required");
        }
        else{
            storeProductInformation();
        }

    }

    private void storeProductInformation()
    {

        //for date and time for PID
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat(" dd,MM,YYYY");
        savecurrentDate = currentdate.format(calendar.getTime());


        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        savecurrentTime = currenttime.format(calendar.getTime());

        productnamekey = savecurrentDate +" "+ savecurrentTime;

        final StorageReference filepath = productImageRef.child(productname.getText().toString() + imageURI.getLastPathSegment() + productnamekey + ".jpg");

        final UploadTask uploadtask = filepath.putFile(imageURI);

        uploadtask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminPanel.this, "Error: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminPanel.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urltask = uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                     if(!task.isSuccessful()){
                         throw task.getException();
                     }

                     downloadImageUrl = filepath.getDownloadUrl().toString();
                     return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            /////////////////////////////most important thing to get the link of the image
                            downloadImageUrl = task.getResult().toString();
                            saveProductInfoToDB();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDB()
    {

        HashMap<String , Object> productMap = new HashMap<>();
        productMap.put("pid" ,productname.getText().toString().trim() + productnamekey.trim());
        productMap.put("date" , savecurrentDate);
        productMap.put("time" , savecurrentTime);
        productMap.put("description" , productdescrip.getText().toString());
        productMap.put("image" , downloadImageUrl);
        productMap.put("price" , productprice.getText().toString());
        productMap.put("product_name" , productname.getText().toString());

        productref.child(productname.getText().toString().trim() + productnamekey.trim()).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminPanel.this, "Product is added to database", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AdminPanel.this, "Product not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    ///open gallety get the selsected image
    //using start activity for result
    //working on that result by onactivity result
    //store the result data(image uri) in a variable

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , gallerypickup);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == gallerypickup){

                imageURI = data.getData();
                productimage.setImageURI(imageURI);

        }
    }
}
