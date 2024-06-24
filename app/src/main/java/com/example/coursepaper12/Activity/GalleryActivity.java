package com.example.coursepaper12.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.codekidlabs.storagechooser.StorageChooser;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.coursepaper12.Adapter.MyAdapter;
import com.example.coursepaper12.Helper.DataClass;
import com.example.coursepaper12.Helper.ImageSaver;
import com.example.coursepaper12.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class GalleryActivity extends Fragment {

    private GridView gridView;
    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_gallery, container, false);

        gridView = rootView.findViewById(R.id.gridView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), dataList);
        gridView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Images").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                        dataList.add(dataClass);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("GalleryActivity", "Ошибка загрузки из базы данных: " + error.getMessage());
                }
            });
        }
        return rootView;
    }
}
