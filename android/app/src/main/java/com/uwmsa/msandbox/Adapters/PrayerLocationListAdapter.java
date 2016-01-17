package com.uwmsa.msandbox.Adapters;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PrayerLocationListAdapter extends RecyclerView.Adapter<PrayerLocationListAdapter.PrayerLocationDailyRecyclerViewHolder> {

    List<PrayerRoomLocation> prayerLocationList;
    static final String joinEmptyLocation = "addBlack";
    static final String joinNonEmptyLocation = "addOrange";
    static final String leaveNonEmptyLocation = "removeOrange";
    static final String joinSecondNonEmptyLocation = "addOrangeBlur";
    static final String joinSecondEmptyLocation = "addBlackBlur";

    private boolean fromRefresh;
    private ArrayList<Boolean> positionsRefreshed;

    Boolean userPresent;
    List<ParseObject> locationsPresent;

    public PrayerLocationListAdapter(List<PrayerRoomLocation> locations, boolean fromRefresh) {
        prayerLocationList = locations;
        positionsRefreshed = new ArrayList<>(Collections.nCopies(prayerLocationList.size(), false));
        this.fromRefresh = fromRefresh;
        RefreshBuffer(true);
    }

    @Override
    public int getItemCount() {
        return prayerLocationList.size();
    }

    @Override
    public void onBindViewHolder(PrayerLocationDailyRecyclerViewHolder holder, int position) {

        PrayerRoomLocation location = prayerLocationList.get(position);
        String roomNumber = location.getRoomnumber();
        String description = location.getDescription();
        JSONObject entryCodeObj = location.getEntryCodeObj();

        holder.vRoomNumber.setText(roomNumber);
        holder.vDescription.setText(description);

        String entryCodeText = "Code: ";
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        try {
            if (entryCodeObj.getBoolean("none")) {
                holder.vEntryCode.setText(entryCodeText + "None");
            } else if (entryCodeObj.getString("all").equals("null")) {
                switch (dayOfWeek) {
                    case Calendar.SUNDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Sunday"));
                        break;
                    case Calendar.MONDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Monday"));
                        break;
                    case Calendar.TUESDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Tuesday"));
                        break;
                    case Calendar.WEDNESDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Wednesday"));
                        break;
                    case Calendar.THURSDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Thursday"));
                        break;
                    case Calendar.FRIDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Friday"));
                        break;
                    case Calendar.SATURDAY:
                        holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("Saturday"));
                        break;
                    default:
                        holder.vEntryCode.setText(entryCodeText + "None");
                        break;
                }
            } else {
                holder.vEntryCode.setText(entryCodeText + entryCodeObj.getString("all"));
            }
        } catch (JSONException e) {
            holder.vEntryCode.setText(entryCodeText + "None");
            e.printStackTrace();
        }

        if( userPresent != null ) {
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

        holder.mLocation = location;

        if (fromRefresh && !positionsRefreshed.get(position)) {
            AnimateUtils.animate(holder);
            positionsRefreshed.set(position, true);
        }
    }

    @Override
    public PrayerLocationDailyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.prayer_room_card, parent, false);
        return new PrayerLocationDailyRecyclerViewHolder(itemView);

    }


    public class PrayerLocationDailyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView vRoomNumber;
        protected TextView vEntryCode;
        protected TextView vDescription;
        protected ImageButton vButton;
        protected PrayerRoomLocation mLocation;

        public PrayerLocationDailyRecyclerViewHolder(View v) {
            super(v);
            vButton = (ImageButton) v.findViewById(R.id.prayerLocation_present);
            vEntryCode = (TextView) v.findViewById(R.id.prayerLocation_code);
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

        if(!fromConstructor) {
            fromRefresh = false;
        }

        locationsPresent = new ArrayList<ParseObject>();
        ParseCloud.callFunctionInBackground("CheckUserPresent", new HashMap<String, Object>(), new FunctionCallback<ArrayList>() {
            @Override
            public void done(ArrayList result, ParseException e) {
                if (e == null) {
                    userPresent = false;
                    if (result.size() > 0) {
                        userPresent = true;
                    }

                    for (Object i : result) {
                        ParseObject j = (ParseObject) i;
                        locationsPresent.add(j.getParseObject("PrayerRoomLocation"));
                    }
                    notifyItemRangeChanged(0, getItemCount());
                }
            }
        });
    }
}
