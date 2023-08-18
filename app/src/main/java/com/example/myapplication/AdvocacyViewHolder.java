package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdvocacyViewHolder extends RecyclerView.ViewHolder {
    TextView officesName, officials;
    ImageView officialImage;

    public AdvocacyViewHolder(@NonNull View itemView) {
        super(itemView);
        officesName = itemView.findViewById(R.id.officesName);
        officials = itemView.findViewById(R.id.officials);
        officialImage = itemView.findViewById(R.id.officialImage);
    }
}
