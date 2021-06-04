package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView userListView;
    ArrayList<String> userList;
    ArrayList<String> uidList;
    public static ArrayList<String> followingUidList;
    ArrayAdapter arrayAdapter;

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userListView = findViewById(R.id.userListView);
        userListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        userList = new ArrayList<>();
        uidList = new ArrayList<>();
        followingUidList = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference();

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,userList);
        userListView.setAdapter(arrayAdapter);

        mRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                uidList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    uidList.add(key);
                    if(!key.equals(FirebaseAuth.getInstance().getUid())) {
                        String email = postSnapshot.child("email").getValue().toString();
                        userList.add(email);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                String uid = uidList.get(position);
                if(checkedTextView.isChecked()){
                    followingUidList.add(uid);
                    Log.i("FollowingList" , followingUidList.toString());
                }else {
                    followingUidList.remove(uid);
                    Log.i("FollowingList" , followingUidList.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.twittermenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.tweet){
            // handle tweet
            Intent intent = new Intent(HomeActivity.this,WriteTweet.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.feed){
            // handle feed
            Intent intent = new Intent(HomeActivity.this,FeedActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logout){
            // handle logout
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}