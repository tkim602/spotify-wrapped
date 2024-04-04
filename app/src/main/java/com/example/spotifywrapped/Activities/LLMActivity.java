package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spotifywrapped.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LLMActivity extends AppCompatActivity {
    String geminiAPIKey = "AIzaSyDSdlNmp-5NI6OsupefGtjeDKNORIPYzh" + "Y";
    String token;
    Spinner dropdown;
    TextView output;
    Button submit;
    String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_llm);

        Bundle gbundle = getIntent().getExtras();
        token = gbundle.getString("accountToken");
        int accountId = gbundle.getInt("accountID");
        
        //Get Layout Items
        dropdown = (Spinner) findViewById(R.id.llm_dropdown);
        output = (TextView) findViewById(R.id.llm_result_text);
        submit = (Button) findViewById(R.id.llm_generate_button);
        ImageButton exitBtn = (ImageButton) findViewById(R.id.llm_exitBtn);

        exitBtn.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("accountToken", token);
            bundle.putInt("accountID", accountId);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);
            startActivity(i);

        });

        String[] options = new String[]{"Act", "Think", "Dress"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(v -> {
            output.setText("Loading...");
            getTopArtists();
        });

    }

    private void generateLLMText(String input) {
        String template = String.format("Describe how someone that listens to the following music might %s. These are the top tracks that the user has listened to: %s", selectedOption, input);
        GenerativeModel gm = new GenerativeModel("gemini-1.0-pro", geminiAPIKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder().addText(template).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Executor exe = Executors.newSingleThreadExecutor();
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText(resultText);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, exe);
    }

    private void getTopArtists() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    generateLLMText(response.body().string());
                } else {
                    System.out.println("Failure!");
                }
            }
        });

    }


}