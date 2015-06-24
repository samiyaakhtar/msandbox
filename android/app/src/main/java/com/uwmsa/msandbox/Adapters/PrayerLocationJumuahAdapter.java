package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
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
public class PrayerLocationJumuahAdapter extends RecyclerView.Adapter<PrayerLocationJumuahAdapter.PrayerLocationJumuahRecyclerViewHolder> {

    List<PrayerRoomLocation> prayerLocationDailyList;

    public PrayerLocationJumuahAdapter(List<PrayerRoomLocation> locations) {
        prayerLocationDailyList = locations;
    }

    @Override
    public int getItemCount() {
        return prayerLocationDailyList.size();
    }

    @Override
    public void onBindViewHolder(PrayerLocationJumuahRecyclerViewHolder holder, int position) {
        PrayerRoomLocation location = prayerLocationDailyList.get(position);
        String roomNumber = location.getBuilding();
        String description = location.getDescription();

        holder.vRoomNumber.setText(roomNumber);
        holder.vDescription.setText(description);
        holder.mLocation = location;
    }

    @Override
    public PrayerLocationJumuahRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.prayer_room_card, parent, false);
        return new PrayerLocationJumuahRecyclerViewHolder(itemView);
    }

    public class PrayerLocationJumuahRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView vRoomNumber;
        protected TextView vDescription;
        protected PrayerRoomLocation mLocation;

        public PrayerLocationJumuahRecyclerViewHolder(View v) {
            super(v);
            vRoomNumber = (TextView) v.findViewById(R.id.prayerLocation_building);
            vDescription = (TextView) v.findViewById(R.id.prayerLocation_description);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
