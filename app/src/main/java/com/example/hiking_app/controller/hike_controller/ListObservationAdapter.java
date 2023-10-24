package com.example.hiking_app.controller.hike_controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiking_app.R;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;

import java.util.List;

public class ListObservationAdapter extends RecyclerView.Adapter<ListObservationAdapter.ObervationViewholder> {
    private List<Observations> observations;
    private LayoutInflater inflater;
    private Context context;
    public ListObservationAdapter(List<Observations> observations, Context context) {

        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.observations = observations;
    }

    @NonNull
    @Override
    public ObervationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ObervationViewholder holder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ObervationViewholder extends RecyclerView.ViewHolder{
        TextView nameTextView, dateTextView;
        ImageView image;
        public ObervationViewholder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.observationName);
            dateTextView = itemView.findViewById(R.id.observationDate);
            image = itemView.findViewById(R.id.observationImage);

        }
    }
}
