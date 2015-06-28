package com.uwmsa.msandbox.Adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dx179 on 3/27/15.
 */
public class PrayerLocationDailyAdapter extends RecyclerView.Adapter<PrayerLocationDailyAdapter.PrayerLocationDailyRecyclerViewHolder> {

    List<PrayerRoomLocation> prayerLocationDailyList;

    static final String joinEmptyLocation = "addBlack";
    static final String joinNonEmptyLocation = "addOrange";
    static final String leaveNonEmptyLocation = "removeOrange";
    Boolean userPresent;


    public PrayerLocationDailyAdapter(List<PrayerRoomLocation> locations) {
        prayerLocationDailyList = locations;

        ParseCloud.callFunctionInBackground("CheckUserPresent", new HashMap<String, Object>(), new FunctionCallback<String>() {
            @Override
            public void done(String result, ParseException e) {
                if(e == null) {
                    Log.d("RESULT", result);
                }
            }
        });
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

        if((int)location.getUsercount() > 0) {
            holder.vButton.setBackgroundResource(R.drawable.ic_group_add_orange_48dp);
            holder.vButton.setTag(joinNonEmptyLocation);
            holder.vButton.setAlpha(0.85f);
        } else {
            holder.vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
            holder.vButton.setTag(joinEmptyLocation);
            holder.vButton.setAlpha(0.15f);
        }

        //TODO: Add in vStatus functionality, implement logic on cloud

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
        protected TextView vStatus;
        protected TextView vDescription;
        protected ImageButton vButton;
        protected PrayerRoomLocation mLocation;

        public PrayerLocationDailyRecyclerViewHolder(View v) {
            super(v);
            vButton = (ImageButton) v.findViewById(R.id.prayerLocation_present);
            vBuilding = (TextView) v.findViewById(R.id.prayerLocation_building);
            vStatus = (TextView) v.findViewById(R.id.prayerLocation_status);
            vRoomNumber = (TextView) v.findViewById(R.id.prayerLocation_roomNumber);
            vDescription = (TextView) v.findViewById(R.id.prayerLocation_description);

            vButton.setOnClickListener(this);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if(v instanceof ImageButton) {

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("prayerLocationId", mLocation.getObjectId());

                if(vButton.getTag() == joinEmptyLocation || vButton.getTag() == joinNonEmptyLocation) {
                    ParseCloud.callFunctionInBackground("IncrementUserCount", params, new FunctionCallback<String>() {
                        public void done(String result, ParseException e) {
                            if (e == null) {
                                Log.d("RESPONSE", result);

                                //TODO: Add cloud logic to work with adding user to location
//                                vButton.setBackgroundResource(R.drawable.ic_group_remove_orange_48dp);
//                                vButton.setTag(leaveNonEmptyLocation);
//                                vButton.setAlpha(0.85f);

                            }
                        }
                    });

                    vButton.setBackgroundResource(R.drawable.ic_group_remove_orange_48dp);
                    vButton.setTag(leaveNonEmptyLocation);
                    vButton.setAlpha(0.85f);

                } else if ( vButton.getTag() == leaveNonEmptyLocation ) {
                    ParseCloud.callFunctionInBackground("DecrementUserCount", params, new FunctionCallback<String>() {
                        public void done(String result, ParseException e) {
                            if (e == null) {
                                Log.d("RESPONSE", result);
                                //TODO: Add cloud logic to work with removing user to location
//                                if( (int) mLocation.getUsercount() > 0 ) {
//                                    vButton.setBackgroundResource(R.drawable.ic_group_add_orange_48dp);
//                                    vButton.setTag("addOrange");
//                                    vButton.setAlpha(0.85f);
//                                } else {
//                                    vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
//                                    vButton.setTag("addBlack");
//                                    vButton.setAlpha(0.15f);
//                                }
                            }
                        }
                    });

                    if( (int) mLocation.getUsercount() > 1 ) {
                        vButton.setBackgroundResource(R.drawable.ic_group_add_orange_48dp);
                        vButton.setTag("addOrange");
                        vButton.setAlpha(0.85f);
                    } else {
                        vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
                        vButton.setTag("addBlack");
                        vButton.setAlpha(0.15f);
                    }
            }
            } else {
                //TODO: Add fragment opening up MapView with location access codes, timings
            }
        }
    }
}
