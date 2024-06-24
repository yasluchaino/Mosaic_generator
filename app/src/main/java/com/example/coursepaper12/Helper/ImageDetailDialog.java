package com.example.coursepaper12.Helper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.coursepaper12.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageDetailDialog extends Dialog {

    private Context context;
    private String imageURL;
    FloatingActionButton saveButton;
    FloatingActionButton deleteBtn;
    FloatingActionButton shareBtn;
    Date d = new Date();
    ImageView expandedImageView;

    public ImageDetailDialog(@NonNull Context context, String imageURL) {
        super(context);
        this.context = context;
        this.imageURL = imageURL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_detail);
        saveButton = findViewById(R.id.saveButton);
        expandedImageView = findViewById(R.id.expandedImageView);
        saveButton.setOnClickListener(v->{
            ImageSaver imageSaver = new ImageSaver(context)
                    .setFileName("image"+d.getYear()+d.getDay()+d.getHours()+d.getMinutes()+d.getSeconds()+".jpg")
                    .setExternal(true);
            Drawable drawable = expandedImageView.getDrawable();
            Bitmap bitmap = null;
            if (drawable != null)
                bitmap = ((BitmapDrawable)drawable).getBitmap();
            imageSaver.save(bitmap);
            Toast.makeText(context, "Изображение сохранено в вашу галерею", Toast.LENGTH_SHORT).show();
        });
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(v->{
            deletePhotoByUrl(imageURL);
        });

        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(v->{
            image();
        });

        Glide.with(context)
                .load(imageURL)
                .into(expandedImageView);

    }

    private void image(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) expandedImageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        shareImageAndText(bitmap);
    }
    private void shareImageAndText(Bitmap bitmap)
    {
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,"Я создала такое мозаичное изображение! Хочешь также?");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Image Subject");
        intent.setType("image/*");
        context.startActivity(intent.createChooser(intent, "Поделиться через"));
    }
    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(context.getFilesDir(), "blocks");
        Uri uri = null;
        try{
            folder.mkdirs();
            File file = new File(folder, "image1213.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println( "File path: " + file.getAbsolutePath());
            uri = FileProvider.getUriForFile(context,"com.example.myapp.fileprovider",file);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return uri;
    }
    private void deletePhotoByUrl(String photoUrl) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference photosRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            photosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DataClass dataClass = snapshot.getValue(DataClass.class);
                        System.out.println("imageURL =" +dataClass.getImageURL());

                        if (dataClass != null && dataClass.getImageURL().equals(photoUrl)) {
                            // Log для отладки
                            Log.d("DeletePhoto", "Found matching URL: " + photoUrl);

                            // Удалить запись из Realtime Database
                            snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                                // Log для отладки
                                Log.d("DeletePhoto", "Deleted from Realtime Database");

                                // Удалить файл из Firebase Storage
                                StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl);
                                imageReference.delete().addOnSuccessListener(aVoid1 -> {
                                    // Log для отладки
                                    Log.d("DeletePhoto", "Deleted from Firebase Storage");

                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to delete from storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }).addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to delete from database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                            return; // Остановить цикл после нахождения и удаления записи
                        }
                    }

                    // Log для отладки
                    Log.d("DeletePhoto", "No matching URL found: " + photoUrl);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Failed to get data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Пользователь не аутентифицирован, выводим сообщение об ошибке
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


}
