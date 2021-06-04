package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    ListView feedListView;
    ArrayList<String> feedList;
    ArrayAdapter arrayAdapter;
    DatabaseReference mRef;
    ArrayList<String> list = new ArrayList<>(HomeActivity.followingUidList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedListView = findViewById(R.id.feedListView);
        feedList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,feedList);

        feedListView.setAdapter(arrayAdapter);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    if(!key.equals(FirebaseAuth.getInstance().getUid()) && list.contains(key)){
                        FirebaseDatabase.getInstance().getReference().child("user").child(key).child("tweets").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                                    String feed = postSnapshot.child("Tweet").getValue().toString();
                                    feedList.add(feed);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}