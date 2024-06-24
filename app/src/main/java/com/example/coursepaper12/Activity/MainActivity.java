package com.example.coursepaper12.Activity;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.codekidlabs.storagechooser.StorageChooser;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.coursepaper12.Helper.DataClass;
import com.example.coursepaper12.Helper.ImageSaver;
import com.example.coursepaper12.Helper.SettingsDetailDialog;
import com.example.coursepaper12.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends Fragment  implements SettingsDetailDialog.OnImagePickListener {
    class ImageData {
        private Bitmap image;
        private int dominantColor;

        public ImageData(Bitmap image, int dominantColor) {
            this.image = image;
            this.dominantColor = dominantColor;
        }

        public Bitmap getImage() {
            return image;
        }

        public int getDominantColor() {
            return dominantColor;
        }

    }
    private List<ImageData> userImagesData;
    private ImageView imageView;
    private Bitmap mainImage1;
    private Bitmap mosaicImage;
    private LocalDateTime d = LocalDateTime.now();
    private static final int REQUEST_PICK_IMAGE = 1;
    private FloatingActionButton uploadButton;
    private FloatingActionButton getSetMosaicBtn;

    private ImageView uploadImage;
    private ProgressBar progressBar;
    private ProgressBar creatingProgress;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FloatingActionButton setMosaicBtn;
    private int mosSize = 9;
    String path = "flowers";
    SettingsDetailDialog settingsDialog;
    EditText uploadCaption;
    public MainActivity(){
        // require a empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadButton = view.findViewById(R.id.uploadButton);
        uploadImage = view.findViewById(R.id.imageView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mainImage1 = BitmapFactory.decodeResource(getResources(), R.drawable.input5);
        imageView = view.findViewById(R.id.imageView);
        creatingProgress = view.findViewById(R.id.creatingProgress);
        Button folderpickerBtn = view.findViewById(R.id.createBtn);
        folderpickerBtn.setOnClickListener(v -> {
            creatingProgress.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            Runnable createMosaicRunnable = () -> {
                creatingProgress.setProgress(10);
                userImagesData = loadUserImagesData();
                creatingProgress.setProgress(50);
                mosaicImage = createColorMatchedMosaicImage(mainImage1, userImagesData);
                creatingProgress.setProgress(100);
                handler.post(() -> {
                    creatingProgress.setVisibility(View.INVISIBLE);
                    if (mosaicImage != null) {
                        imageView.setImageBitmap(mosaicImage);
                    }
                });
            };
            handler.post(createMosaicRunnable);
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    uploadToFirebase(saveBitmapToFile(mosaicImage));
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Создайте изображение", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton saveBtn = view.findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(v -> {
                System.out.println(d.getDayOfYear() + "." + d.getHour() + "." + d.getMinute());
                ImageSaver imageSaver = new ImageSaver(requireContext())
                        .setFileName("image" + d.getDayOfYear() + d.getHour() + d.getMinute() + ".jpg")
                        .setExternal(true);
                ImageView image = view.findViewById(R.id.imageView);
                try {
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Toast.makeText(requireContext(), "Изображение сохранено в вашу галерею", Toast.LENGTH_SHORT).show();
                    imageSaver.save(bitmap);
                }
                catch (Exception e){
                    Toast.makeText(requireContext(), "Изображения не существует", Toast.LENGTH_SHORT).show();

                }
            });
        getSetMosaicBtn = view.findViewById(R.id.setMosaicBtn);
        getSetMosaicBtn.setOnClickListener(view1 -> {
             settingsDialog = new SettingsDetailDialog(getContext(), mainImage1, new SettingsDetailDialog.SettingsDialogListener() {
                @Override
                public void onSettingsApplied(String mainImagePath, String dataset) {
                }
            });

            settingsDialog.setOnImagePickListener(new SettingsDetailDialog.OnImagePickListener() {
                @Override
                public void onImagePicked(Bitmap image) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_PICK_IMAGE);
                    if (image!=null)
                        settingsDialog.setMainImage(image);
                }

            });
            settingsDialog.setOnMosaicSizeChangeListener(new SettingsDetailDialog.OnMosaicSizeChangeListener() {
                @Override
                public void onMosaicSizeChanged(int newSize) {
                    mosSize = newSize;
                }
            });
            settingsDialog.setOnDatasetChangeListener(new SettingsDetailDialog.OnDatasetChangeListener() {
                @Override
                public void onDatasetChanged(String _path) {
                    path = _path;
                }
            });
            settingsDialog.show();
        });

        setupFirebase();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                if (settingsDialog != null && settingsDialog.isShowing()) {
                    mainImage1 = bitmap;
                    settingsDialog.setMainImage(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Uri saveBitmapToFile(Bitmap bitmap) {
        File file = new File(requireContext().getFilesDir(), "image.jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();
            return Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        storageReference = FirebaseStorage.getInstance().getReference();
    }
    private List<ImageData> loadUserImagesData() {
        List<ImageData> imageDataList = new ArrayList<>();
        AssetManager assetManager = requireContext().getAssets();
        try {
            String[] imageFiles = assetManager.list(path);
            if (imageFiles != null) {
                for (String imageFile : imageFiles) {
                    InputStream inputStream = assetManager.open(path +'/' + imageFile);
                    Bitmap userImage = BitmapFactory.decodeStream(inputStream);
                    if (userImage != null) {
                        int dominantColor = getDominantColor(userImage);
                        imageDataList.add(new ImageData(userImage, dominantColor));
                    }
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageDataList;
    }
    private int getDominantColor(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int redSum = 0, greenSum = 0, blueSum = 0;
        int pixelCount = width * height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = bitmap.getPixel(x, y);
                redSum += Color.red(color);
                greenSum += Color.green(color);
                blueSum += Color.blue(color);
            }
        }

        int clampedRed = Math.min(255, Math.max(0, redSum / pixelCount));
        int clampedGreen = Math.min(255, Math.max(0, greenSum / pixelCount));
        int clampedBlue = Math.min(255, Math.max(0, blueSum / pixelCount));

        int averageColor = Color.rgb(clampedRed, clampedGreen, clampedBlue);
        return averageColor;
    }
    private Bitmap createColorMatchedMosaicImage(Bitmap mainImage, List<ImageData> userImagesData) {
        Bitmap mosaicImage = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.ARGB_8888);

        for (int y = 0; y < mainImage.getHeight(); y += mosSize) {
            for (int x = 0; x < mainImage.getWidth(); x += mosSize) {
                int blockColor = getBlockColor(mainImage, x, y, mosSize);
                Bitmap bestMatchedImage = findBestMatchingImage(userImagesData, blockColor);
                Bitmap scaledImage = Bitmap.createScaledBitmap(bestMatchedImage, mosSize, mosSize, true);
                Canvas canvas = new Canvas(mosaicImage);
                canvas.drawBitmap(scaledImage, x, y, null);

            }
        }

        return mosaicImage;
    }
    private int getBlockColor(Bitmap image, int startX, int startY, int blockSize) {
        // Вычисляем средний цвет блока основного изображения
        int redSum = 0, greenSum = 0, blueSum = 0;
        int pixelCount = 0;

        for (int y = startY; y < startY + blockSize && y < image.getHeight(); y++) {
            for (int x = startX; x < startX + blockSize && x < image.getWidth(); x++) {
                int color = image.getPixel(x, y);
                redSum += Color.red(color);
                greenSum += Color.green(color);
                blueSum += Color.blue(color);
                pixelCount++;
            }
        }

        int clampedRed = Math.min(255, Math.max(0, redSum / pixelCount));
        int clampedGreen = Math.min(255, Math.max(0, greenSum / pixelCount));
        int clampedBlue = Math.min(255, Math.max(0, blueSum / pixelCount));

        int averageColor = Color.rgb(clampedRed, clampedGreen, clampedBlue);
        return averageColor;
    }

    private Bitmap findBestMatchingImage(List<ImageData> userImagesData, int targetColor) {
        Bitmap bestMatchedImage = null;
        int minColorDistance = Integer.MAX_VALUE;

        for (ImageData imageData : userImagesData) {
            int userImageColor = imageData.getDominantColor();
            int colorDistance = calculateColorDistance(userImageColor, targetColor);

            if (colorDistance < minColorDistance) {
                minColorDistance = colorDistance;
                bestMatchedImage = imageData.getImage();
            }
        }

        return bestMatchedImage;
    }
    private int calculateColorDistance(int color1, int color2) {
        int r1 = Color.red(color1);
        int g1 = Color.green(color1);
        int b1 = Color.blue(color1);

        int r2 = Color.red(color2);
        int g2 = Color.green(color2);
        int b2 = Color.blue(color2);

        return (int) Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));
    }

    @Override
    public void onImagePicked(Bitmap image) {
        mainImage1 = image;
        imageView.setImageBitmap(image);
    }

    private void uploadToFirebase(Uri uri){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            StorageReference userStorageRef = storageReference.child("users").child(uid);
            String fileName = d.getDayOfYear()+"-"+d.getHour()+"-"+d.getMinute()+"-"+d.getSecond() + ".jpg";
            StorageReference imageReference = userStorageRef.child(fileName);
            imageReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            DataClass dataClass = new DataClass(downloadUri.toString());
                            String key = databaseReference.push().getKey();
                            if (key != null) {
                                databaseReference.child(uid).child(key).setValue(dataClass);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(requireContext(), "Изображение загружено", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnProgressListener(snapshot -> {
                        progressBar.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Пользователь не аутентифицирован", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }


}