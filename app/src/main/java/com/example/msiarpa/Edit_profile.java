package com.example.msiarpa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Edit_profile extends AppCompatActivity {

    private ImageView profileImage;
    private Button editProfileButton;
    private Button uploadImageButton;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String currentPhotoPath;

    private static final int REQUEST_CROP_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImage = findViewById(R.id.profile_image);
        editProfileButton = findViewById(R.id.edit_profile_button);
        uploadImageButton = findViewById(R.id.upload_image_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri!= null) {
                    uploadImageToFirebase(imageUri);
                } else {
                    Toast.makeText(Edit_profile.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!= null) {
            imageUri = data.getData();
            cropImage(imageUri);
        } else if (requestCode == REQUEST_CROP_IMAGE && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            profileImage.setImageURI(croppedUri);
            uploadImageToFirebase(croppedUri);
        }
    }

    private void cropImage(Uri uri) {
        String destinationFileName = SimpleDateFormat.getDateTimeInstance().format(new Date()) + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1f, 1f);
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        uCrop.withOptions(options);
        uCrop.start(this, REQUEST_CROP_IMAGE);
    }
    private void uploadImageToFirebase(Uri imageUri) {
        try {
            // Get the bitmap from the Uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            // Convert the bitmap to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();

            // Upload the byte array to Firebase Storage
            StorageReference ref = storageReference.child("images/" + mAuth.getCurrentUser().getUid() + ".jpg");
            UploadTask uploadTask = ref.putBytes(data);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Edit_profile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        // Update the user's profile image in Firestore
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("students").document(mAuth.getCurrentUser().getUid()).update("profileImage", uri.toString());
                            }
                        });
                    } else {
                        Toast.makeText(Edit_profile.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}