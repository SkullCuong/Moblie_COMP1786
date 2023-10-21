package com.example.hiking_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiking_app.model.Hikes;

import java.util.List;

public class ListHikeAdapter extends RecyclerView.Adapter<ListHikeAdapter.HikesViewHolder> {

    private List<Hikes> hikes;
    private LayoutInflater inflater;
    private Context context;
    private List<Hikes> filteredList;
    public ListHikeAdapter(List<Hikes> hikes, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.hikes = hikes;
    }
    @NonNull
    @Override
    public HikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_view_hike, null);
        return new ListHikeAdapter.HikesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HikesViewHolder holder, int position) {
        holder.bindData(hikes.get(position));
    }
    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public class HikesViewHolder extends RecyclerView.ViewHolder {
        ImageView HikeImg;
        TextView name, location, date, length;

        public HikesViewHolder(@NonNull View itemView) {
            super(itemView);
            HikeImg = itemView.findViewById(R.id.hikeImage);
            name = itemView.findViewById(R.id.hikeName);
            location = itemView.findViewById(R.id.hikeLocation);
            date = itemView.findViewById(R.id.hikeDate);
            length = itemView.findViewById(R.id.hikeLength);

        }
        public void setFilteredList(List<Hikes> filteredList){

        }
        void bindData(final Hikes hike) {
            //personImage.setImageBitmap(getUserImage(hike.getImage()));
//            HikeImg.setImageBitmap(getUserImage());
            name.setText(hike.getName());
            date.setText(hike.getDate());
            location.setText(hike.getLocation());
            length.setText(String.valueOf(hike.getLength()));
        }
    }
    public void setFilteredList(List<Hikes> filteredList) {
        this.hikes.clear();
        this.hikes.addAll(filteredList);
        notifyDataSetChanged();
    }
    private Bitmap getUserImage(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
}
