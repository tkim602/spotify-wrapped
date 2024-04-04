package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.R;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {
    private RecyclerView screenshotRecyclerView;
    private List<String> screenshotPaths;
    private String location;
    private String screenshotPath;

    private String AccessToken;
    private String timeFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        location = bundle.getString("location");
        AccessToken = bundle.getString("accountToken");
        int accountID = bundle.getInt("accountID");
        timeFrame = bundle.getString("timeFrame");
        if (location.equals("summary")){
            bundle = new Bundle();
            bundle.putString("accountToken", AccessToken);
            bundle.putString("timeFrame", timeFrame);
            bundle.putInt("accountID", accountID);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);
            startActivity(i);
        }
        setContentView(R.layout.gallerypage);
    }
}
