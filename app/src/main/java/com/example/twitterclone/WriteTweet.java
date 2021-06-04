package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class WriteTweet extends AppCompatActivity {

    EditText edtTweet;
    Button btnPostTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        btnPostTweet = findViewById(R.id.btnPostTweet);

        btnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet();
            }
        });
    }
    private void postTweet(){
        String tweet = edtTweet.getText().toString();
        HashMap<String,String> map = new HashMap<>();
        map.put("Tweet" , tweet);
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("tweets").push().setValue(map);
    }
}