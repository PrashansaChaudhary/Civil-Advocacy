package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    TextView curLocation, nameofOffices, officialsName;
    ImageView official_image,partyLogo;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Civil Advocacy");
        setContentView(R.layout.activity_photo);

        curLocation = findViewById(R.id.currentLocation);
        nameofOffices = findViewById(R.id.officesNames);
        officialsName = findViewById(R.id.officialsName);
        official_image = findViewById(R.id.official_image);
        partyLogo = findViewById(R.id.partyLogo);
        constraintLayout = findViewById(R.id.constrainedLayout);

        Intent photoIntent = getIntent();
        if(photoIntent.hasExtra("location")){
            String location = photoIntent.getStringExtra("location");
            curLocation.setText(location);
        }
        if(photoIntent.hasExtra("title")){
            nameofOffices.setText(photoIntent.getStringExtra("title"));
        }
        if(photoIntent.hasExtra("name")){
            officialsName.setText(photoIntent.getStringExtra("name"));
        }
        if(photoIntent.hasExtra("url")){
            Picasso.get().load(photoIntent.getStringExtra("url"))
                    .error(R.drawable.brokenimage)
                    .into(official_image);
        }
        if(photoIntent.hasExtra("partyLogo")){
            if(photoIntent.getStringExtra("partyLogo").contains("democratic")){
                partyLogo.setImageResource(R.drawable.democratic_logo);
                constraintLayout.setBackgroundResource(R.color.blue);
            } else if(photoIntent.getStringExtra("partyLogo").contains("republican")){
                partyLogo.setImageResource(R.drawable.republican_logo);
                constraintLayout.setBackgroundResource(R.color.red);
            } else {
                constraintLayout.setBackgroundResource(R.color.grey);
            }
        }

    }
}