package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import civicinfo.NormalizedInput;
import civicinfo.Officials;
import officialsdata.Channel;

public class IndividualOfficials extends AppCompatActivity {

    NormalizedInput normalizedInput;
    Officials officials;
    TextView currentLocation, officesNames, officialsName, officialsParty;
    TextView address, detailsofAddress, phone, detailsofPhone, website, detailsofWebsite, email , detailsofEmail;
    ImageView official_image, partyLogo, facebookIcon, twitterIcon, youtubeIcon;
    ConstraintLayout constraintLayout;
    Channel facebook, youtube, twitter;
    String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_officials);

        currentLocation = findViewById(R.id.currentLocation);

        officesNames = findViewById(R.id.officesNames);
        officialsName = findViewById(R.id.officialsName);
        officialsParty = findViewById(R.id.officialsParty);
        official_image = findViewById(R.id.official_image);

        address = findViewById(R.id.address);
        detailsofAddress = findViewById(R.id.addressDetails);

        // Phone
        phone = findViewById(R.id.phone);
        detailsofPhone = findViewById(R.id.phoneDetails);

        // Emails
        email = findViewById(R.id.email);
        detailsofEmail = findViewById(R.id.emailDetails);

        // Website
        website = findViewById(R.id.website);
        detailsofWebsite = findViewById(R.id.websiteDetails);

        partyLogo = findViewById(R.id.partyLogo);

        constraintLayout = findViewById(R.id.constrainedLayout);

        facebookIcon = findViewById(R.id.facebook);
        twitterIcon = findViewById(R.id.twitter);
        youtubeIcon = findViewById(R.id.youtube);

        Intent civilIntent = getIntent();
        if(civilIntent.hasExtra("locationName")){
            this.normalizedInput = (NormalizedInput)civilIntent.getSerializableExtra("locationName");

            if(!normalizedInput.getLine1().isEmpty()){
                location = normalizedInput.getLine1() + ", " + normalizedInput.getCity()+", "+normalizedInput.getState() + " " + normalizedInput.getZip();
            } else {
                location = normalizedInput.getCity()+", "+normalizedInput.getState();
            }

            this.currentLocation.setText(location);
        }
        if(civilIntent.hasExtra("officials")){
            this.officials = (Officials) civilIntent.getSerializableExtra(String.valueOf("officials"));
            setOfficialsData(officials);
        }

    }
    private void setOfficialsData(Officials officials) {
        String title =officials.getTitle() ;
        String name = officials.getName();
        String party = officials.getParty();

        officesNames.setText(title);
        officialsName.setText(name);
        officialsParty.setText(party);
        String url = "";

        if(officials.getPhotoUrl().trim().equals("")){
            Picasso.get().load("temp")
                    .error(R.drawable.missing)
                    .into(official_image);
        } else {
            url = officials.getPhotoUrl().trim().replace("http:","https:");
            Picasso.get().load(url)
                    .error(R.drawable.brokenimage)
                    .into(official_image);
        }

        String finalUrl = url;
        if(!officials.getPhotoUrl().isEmpty()){
            official_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoIntent = new Intent(IndividualOfficials.this, PhotoActivity.class);
                    photoIntent.putExtra("location", location);
                    photoIntent.putExtra("title", title);
                    photoIntent.putExtra("name", name);
                    photoIntent.putExtra("url", finalUrl);
                    photoIntent.putExtra("partyLogo",officials.getParty().trim().toLowerCase());

                    startActivity(photoIntent);
                }
            });
        }

        // Setting address Details
        if(!officials.getAddress().isEmpty()){
            //address.setTextColor(Color.BLACK);
            detailsofAddress.setText(officials.getAddress());
            detailsofAddress.setTextColor(Color.WHITE);
            Linkify.addLinks(detailsofAddress, Linkify.ALL);
        } else {
            address.setVisibility(View.GONE);
            detailsofAddress.setVisibility(View.GONE);
        }

        // Phone Details
        if(!officials.getPhones().isEmpty()){
            detailsofPhone.setText(officials.getPhones());
            detailsofPhone.setTextColor(Color.BLACK);
            Linkify.addLinks(detailsofPhone, Linkify.ALL);
        } else {
            phone.setVisibility(View.GONE);
            detailsofPhone.setVisibility(View.GONE);
        }

        // Emails
        if(!officials.getEmails().isEmpty()){
            detailsofEmail.setText(officials.getEmails());
            detailsofEmail.setTextColor(Color.BLACK);
            Linkify.addLinks(detailsofEmail, Linkify.ALL);
            detailsofEmail.setLinkTextColor(Color.WHITE);
        } else {
            email.setVisibility(View.GONE);
            detailsofEmail.setVisibility(View.GONE);
        }

        // Website
        if(!officials.getUrls().isEmpty()){
            detailsofWebsite.setText(officials.getUrls());
            detailsofWebsite.setTextColor(Color.BLACK);
            Linkify.addLinks(detailsofWebsite, Linkify.ALL);
        } else {
            website.setVisibility(View.GONE);
            detailsofWebsite.setVisibility(View.GONE);
        }

        if(officials.getParty().trim().toLowerCase().contains("democratic")){
            currentLocation.setBackgroundResource(R.color.lightTheme);
            partyLogo.setImageResource(R.drawable.democratic_logo);
            constraintLayout.setBackgroundResource(R.color.blue);
            getWindow().setNavigationBarColor(getColor(R.color.blue));
        } else if(officials.getParty().trim().toLowerCase().contains("republican")){
            currentLocation.setBackgroundResource(R.color.lightTheme);
            partyLogo.setImageResource(R.drawable.republican_logo);
            constraintLayout.setBackgroundResource(R.color.red);
            getWindow().setNavigationBarColor(getColor(R.color.red));
        } else if(officials.getParty().trim().toLowerCase().contains("nonpartisan")) {
            currentLocation.setBackgroundResource(R.color.lightTheme);
            constraintLayout.setBackgroundResource(R.color.grey);
            getWindow().setNavigationBarColor(getColor(R.color.grey));
        }

        setChannels(officials.getChannels());
    }

    private void setChannels(ArrayList<Channel> channels) {
        int channelSize = channels.size();
        if(channelSize != 0){
            for(Channel single_channel : channels ) {
                if(single_channel.getType().equals("Facebook")) {
                    facebook = single_channel;
                    if(facebookIcon.getVisibility() != View.GONE){
                        facebookIcon.setVisibility(View.VISIBLE);
                    } else {
                        facebookIcon.setVisibility(View.INVISIBLE);
                        continue;
                    }

                    facebookIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            String name = single_channel.getId();
                            try {
                                // get the Facebook app if possible
                                getPackageManager().getPackageInfo("com.facebook.katana", 0);
                                String FACEBOOK_URL = "https://www.facebook.com/" + name;
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_URL));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } catch (Exception e) {         // no Twitter app, revert to browser
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebok.com/" + name));
                            }
                            startActivity(intent);
                        }
                    });
                }
                if(single_channel.getType().equals("Twitter")) {
                    twitter = single_channel;
                    //twitterIcon.setVisibility(View.VISIBLE);
                    if(twitterIcon.getVisibility() != View.GONE){
                        twitterIcon.setVisibility(View.VISIBLE);
                    } else {
                        continue;
                    }
                    twitterIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            String name = single_channel.getId();
                            try {
                                // get the Twitter app if possible
                                getPackageManager().getPackageInfo("com.twitter.android", 0);
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } catch (Exception e) {         // no Twitter app, revert to browser
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
                            }
                            startActivity(intent);
                            }
                    });
                }
                if(single_channel.getType().equals("YouTube")) {
                    youtube = single_channel;
                    //youtubeIcon.setVisibility(View.VISIBLE);
                    if(youtubeIcon.getVisibility() != View.GONE){
                        youtubeIcon.setVisibility(View.VISIBLE);
                    } else {
                        continue;
                    }
                    youtubeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = single_channel.getId();
                            Intent intent = null;
                            try{
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setPackage("com.google.android.youtube");
                                intent.setData(Uri.parse("https://www.youtube.com/" + name));
                                startActivity(intent);

                            } catch (ActivityNotFoundException e){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                            }
                        }
                    });
                }
            }
        }
    }

    public void partyLogoClick(View v) {
        if (officials.getParty().toLowerCase().contains("democratic")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org"));
            startActivity(intent);
        } else if(officials.getParty().toLowerCase().contains("republican")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com"));
            startActivity(intent);
        }
    }

}