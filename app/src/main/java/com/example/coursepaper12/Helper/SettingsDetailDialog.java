package com.example.coursepaper12.Helper;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.coursepaper12.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SettingsDetailDialog extends Dialog {
    private OnImagePickListener imagePickListener;
    private OnDatasetChangeListener mosaicDatasetListener;

    public interface SettingsDialogListener {
        void onSettingsApplied(String mainImagePath, String dataset);
    }
    public void setOnSettingsAppliedListener(SettingsDialogListener listener) {
        this.mListener = listener;
    }
    public interface OnImagePickListener {
        void onImagePicked(Bitmap image);
    }
    public void setOnImagePickListener(OnImagePickListener listener) {
        this.imagePickListener = listener;
    }
    public interface OnMosaicSizeChangeListener {
        void onMosaicSizeChanged(int newSize);
    }
    public void setOnMosaicSizeChangeListener(OnMosaicSizeChangeListener listener) {
        this.mosaicSizeChangeListener = listener;
    }
    public void setOnDatasetChangeListener(OnDatasetChangeListener listener) {
        this.mosaicDatasetListener = listener;
    }
    public interface OnDatasetChangeListener {
        void onDatasetChanged(String path);
    }
    private Context context;
    private String mainImagePath;
    private String dataset;
    Bitmap mainImage;
    private int mosaicSize=7;
    private static final int REQUEST_PICK_IMAGE = 1;
    private OnMosaicSizeChangeListener mosaicSizeChangeListener;


    ImageView mainImage1;
    TextView pickMainImage;
    TextView pickDataset;
    EditText changeMosaicSize;
    AppCompatButton applyButton;
    RadioButton radioButtonFlowers;
    RadioButton radioButtonCats;
    RadioButton radioButtonBlocks;
    String path;
    private SettingsDialogListener mListener;


    public SettingsDetailDialog(@NonNull Context context, Bitmap mainImage, SettingsDialogListener listener) {
        super(context);
        this.context = context;
        this.mainImage = mainImage;
       // this.dataset = dataset;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings_detail);

        pickMainImage = findViewById(R.id.pickMainImage);
        pickDataset = findViewById(R.id.pickDataset);
        changeMosaicSize = findViewById(R.id.sizeEdt);
        applyButton = findViewById(R.id.applyButton);
        mainImage1 = findViewById(R.id.imagePicker);
        radioButtonFlowers = findViewById(R.id.radioButtonFlowers);
        radioButtonCats = findViewById(R.id.radioButtonCats);
        radioButtonBlocks = findViewById(R.id.radioButtonBlocks);

        if (mainImage != null) {
            mainImage1.setImageBitmap(mainImage);
        }
        applyButton.setOnClickListener(v -> {
            mListener.onSettingsApplied(mainImagePath, dataset);
            int newSize = Integer.parseInt(changeMosaicSize.getText().toString());
            if (mosaicSizeChangeListener != null) {
                mosaicSizeChangeListener.onMosaicSizeChanged(newSize);
            }
            mosaicDatasetListener.onDatasetChanged(path);
            dismiss();
        });
        pickMainImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (imagePickListener != null) {
                imagePickListener.onImagePicked(mainImage);
            }
        });
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                path = "flowers";
                if (checkedId == R.id.radioButtonFlowers)
                {
                    path = "flowers";
                }
                if (checkedId == R.id.radioButtonCats)
                {
                    path = "cats";                }
                if (checkedId == R.id.radioButtonBlocks)
                {
                    path = "blocks";
                }
            }
        });

    }
    public void setMainImage(Bitmap image) {
        mainImage = image;
        mainImage1.setImageBitmap(image);
    }
}
