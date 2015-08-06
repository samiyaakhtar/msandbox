package com.uwmsa.msandbox.Adapters;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.AnimateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrayerLocationDailyAdapter extends RecyclerView.Adapter<PrayerLocationDailyAdapter.PrayerLocationDailyRecyclerViewHolder> {

    List<PrayerRoomLocation> prayerLocationDailyList;
    static final String joinEmptyLocation = "addBlack";
    static final String joinNonEmptyLocation = "addOrange";
    static final String leaveNonEmptyLocation = "removeOrange";
    static final String joinSecondNonEmptyLocation = "addOrangeBlur";
    static final String joinSecondEmptyLocation = "addBlackBlur";

    private boolean fromRefresh;

    Boolean userPresent;
    List<ParseObject> locationsPresent;

    public PrayerLocationDailyAdapter(List<PrayerRoomLocation> locations, boolean fromRefresh) {
        prayerLocationDailyList = locations;
        this.fromRefresh = fromRefresh;
        RefreshBuffer(true);
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

        if( userPresent != null ){
            if (!userPresent) {
                if ((int) location.getUsercount() > 0) {
                    holder.vButton.setBackgroundResource(R.drawable.ic_group_add_orange_48dp);
                    holder.vButton.setTag(joinNonEmptyLocation);
                    holder.vButton.setAlpha(0.85f);
                } else {
                    holder.vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
                    holder.vButton.setTag(joinEmptyLocation);
                    holder.vButton.setAlpha(0.25f);
                }
            } else {
                if (locationsPresent.indexOf(location) != -1) {
                    holder.vButton.setBackgroundResource(R.drawable.ic_group_remove_orange_48dp);
                    holder.vButton.setTag(leaveNonEmptyLocation);
                    holder.vButton.setAlpha(0.85f);
                } else {
                    if ((int) location.getUsercount() > 0) {
                        holder.vButton.setBackgroundResource(R.drawable.ic_group_add_orange_48dp);
                        holder.vButton.setTag(joinSecondNonEmptyLocation);
                        holder.vButton.setAlpha(0.65f);
                    } else {
                        holder.vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
                        holder.vButton.setTag(joinSecondEmptyLocation);
                        holder.vButton.setAlpha(0.15f);
                    }
                }
            }
        } else {
//            holder.vButton.setBackgroundResource(R.drawable.ic_group_add_black_48dp);
//            holder.vButton.setTag(joinEmptyLocation);
//            holder.vButton.setAlpha(0.15f);
        }

        //TODO: Add in vStatus functionality, implement logic on cloud

        holder.mLocation = location;

        if(fromRefresh)
            AnimateUtils.animate(holder);
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
                    ParseCloud.callFunctionInBackground("IncrementUserCount", params, new FunctionCallback<PrayerRoomLocation>() {
                        public void done(PrayerRoomLocation result, ParseException e) {
                            if (e == null) {
                                mLocation = result;
                                RefreshBuffer(false);
                            }
                        }
                    });

                } else if ( vButton.getTag() == leaveNonEmptyLocation ) {
                    ParseCloud.callFunctionInBackground("DecrementUserCount", params, new FunctionCallback<PrayerRoomLocation>() {
                        public void done(PrayerRoomLocation result, ParseException e) {
                            if (e == null) {
                                mLocation = result;
                                RefreshBuffer(false);
                            }
                        }
                    });
                } else {
                    Log.d("ERR", "Different places");
                }

            } else {
                //TODO: Add fragment opening up MapView with location access codes, timings
            }
        }
    }


    public void RefreshBuffer(boolean fromConstructor) {
        Log.d("called", "refresh buffer");
        if(!fromConstructor)
            fromRefresh = false;
        locationsPresent = new ArrayList<ParseObject>();
        ParseCloud.callFunctionInBackground("CheckUserPresent", new HashMap<String, Object>(), new FunctionCallback<ArrayList>() {
            @Override
            public void done(ArrayList result, ParseException e) {
                if(e == null) {
                    if(result.size() > 0) {
                        userPresent = true;
                    } else
                        userPresent = false;

                    for(Object i : result) {
                        ParseObject j = (ParseObject)i;
                        locationsPresent.add(j.getParseObject("PrayerRoomLocation"));
                    }
                    notifyItemRangeChanged(0, getItemCount());
                }
            }
        });
    }
}
