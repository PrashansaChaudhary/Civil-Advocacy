package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.application_name));
        setContentView(R.layout.activity_about);
        TextView linkTextView = findViewById(R.id.googleCivicApi);
        linkTextView.setLinkTextColor(Color.WHITE);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void reDirect(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://developers.google.com/civic-information/"));
        startActivity(intent);
    }
}