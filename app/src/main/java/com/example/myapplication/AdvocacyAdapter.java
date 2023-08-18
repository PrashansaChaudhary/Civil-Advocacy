package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import civicinfo.Offices;
import civicinfo.Officials;

public class AdvocacyAdapter extends RecyclerView.Adapter<AdvocacyViewHolder> {
    private final MainActivity mainActivity;
    List<Offices> officesList;
    List<Officials> officialsList;


    public AdvocacyAdapter(MainActivity mainActivity, List<Offices> officesList, List<Officials> officialsList) {
        this.mainActivity = mainActivity;
        this.officesList = officesList;
        this.officialsList = officialsList;
    }

    @NonNull
    @Override
    public AdvocacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View items = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_recyclerview_items, parent, false);
        items.setOnClickListener(mainActivity);
        return new AdvocacyViewHolder(items);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvocacyViewHolder holder, int position) {
        Officials official = officialsList.get(position);
        if(official.getTitle() != null){
            holder.officesName.setText(official.getTitle());
        }

        holder.officials.setText(official.getName()+official.getParty());
        if(!official.getPhotoUrl().isEmpty()){
            Picasso.get().load(official.getPhotoUrl()).placeholder(R.drawable.missing).error(R.drawable.brokenimage).into(holder.officialImage);
        }
        else{
            holder.officialImage.setImageResource(R.drawable.missing);
        }


    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }
}
