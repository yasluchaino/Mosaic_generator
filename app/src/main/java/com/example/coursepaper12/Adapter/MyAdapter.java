package com.example.coursepaper12.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coursepaper12.Helper.DataClass;
import com.example.coursepaper12.Helper.ImageDetailDialog;
import com.example.coursepaper12.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<DataClass> dataList;
    private Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }

        ImageView gridImage = view.findViewById(R.id.gridImage);

        // Загрузка изображения с помощью Glide
        Glide.with(context)
                .load(dataList.get(i).getImageURL())
                .into(gridImage);

        // Обработчик нажатия на изображение в GridView
        gridImage.setOnClickListener(v -> {
            // Открываем диалоговое окно или активити для отображения увеличенного изображения
            showImageDetailDialog(dataList.get(i).getImageURL());
        });

        return view;
    }

    private void showImageDetailDialog(String imageURL) {
        ImageDetailDialog dialog = new ImageDetailDialog(context, imageURL);
        dialog.show();
    }
}