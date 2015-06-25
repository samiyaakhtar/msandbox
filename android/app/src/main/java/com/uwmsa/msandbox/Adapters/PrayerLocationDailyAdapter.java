package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;

import java.util.List;

/**
 * Created by dx179 on 3/27/15.
 */
public class PrayerLocationDailyAdapter extends RecyclerView.Adapter<PrayerLocationDailyAdapter.PrayerLocationDailyRecyclerViewHolder> {

    List<PrayerRoomLocation> prayerLocationDailyList;

    public PrayerLocationDailyAdapter(List<PrayerRoomLocation> locations) {
        prayerLocationDailyList = locations;
    }

    @Override
    public int getItemCount() {
        return prayerLocationDailyList.size();
    }

    @Override
    public void onBindViewHolder(PrayerLocationDailyRecyclerViewHolder holder, int position) {
        PrayerRoomLocation location = prayerLocationDailyList.get(position);
        String building = location.getBuilding();
        String roomNumber = location.getRoomnumber();
        String description = location.getDescription();

        holder.vBuilding.setText(building);
        holder.vRoomNumber.setText("- " + roomNumber);
        holder.vDescription.setText(description);
        holder.mLocation = location;
    }

    @Override
    public PrayerLocationDailyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.prayer_room_card, parent, false);

        return new PrayerLocationDailyRecyclerViewHolder(itemView);

    }


    public class PrayerLocationDailyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView vBuilding;
        protected TextView vRoomNumber;
        protected TextView vDescription;
        protected PrayerRoomLocation mLocation;

        public PrayerLocationDailyRecyclerViewHolder(View v) {
            super(v);
            vBuilding = (TextView) v.findViewById(R.id.prayerLocation_building);
            vRoomNumber = (TextView) v.findViewById(R.id.prayerLocation_roomNumber);
            vDescription = (TextView) v.findViewById(R.id.prayerLocation_description);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Log.d("TAG", "CLICKED " + vBuilding.getText().toString());
        }
    }
}
