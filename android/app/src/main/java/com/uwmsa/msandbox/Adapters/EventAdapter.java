package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.SaveCallback;
import com.uwmsa.msandbox.Models.*;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.AnimateUtils;
import com.uwmsa.msandbox.Utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by samiya on 28/02/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventRecyclerViewHolder>  {

    private OnEventClickListener mOnClickListener;
    private boolean fromRefresh;

    public static interface OnEventClickListener {
        void OnEventClickListener(final Event event);
    }

    public void setOnEventClickListener(final OnEventClickListener listener) {
        this.mOnClickListener = listener;
    }

    /**
     * Created by samiya on 28/02/15.
     */
    public class EventRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView vTitle;
        protected ParseImageView vImageView;
        protected TextView vStartTime;
        protected TextView vCategory;
        protected ImageView vLike;
        protected Event mEvent;

        public void setEvent(Event e) {
            this.mEvent = e;
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.OnEventClickListener(mEvent);
        }

        public EventRecyclerViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.event_title);
            vImageView = (ParseImageView) v.findViewById(R.id.event_thumbnail);
            vStartTime = (TextView) v.findViewById(R.id.event_startTime);
            vCategory = (TextView) v.findViewById(R.id.event_category);
            vLike = (ImageView) v.findViewById(R.id.event_likeImageView);

            vLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventLike like = new EventLike();
                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(false);
                    acl.setPublicWriteAccess(false);
                    acl.setReadAccess(User.getCurrentUser(), true);
                    acl.setWriteAccess(User.getCurrentUser(), true);

                    like.setUser();
                    like.setEvent(mEvent);
                    like.setACL(acl);
                    like.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("SUCCESS", "Saved like");
                            } else {
                                Log.e("ERROR", e.getMessage());
                            }
                        }
                    });
                }
            });

            v.setOnClickListener(this);
        }
    }

    private List<Event> eventList;

    public EventAdapter(List<Event> listItems, boolean fromRefresh) {
        this.eventList = listItems;
        this.fromRefresh = fromRefresh;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);
        String eventDetails = event.getTitle() ;
        String eventStartTime = Utilities.getStringFromDate(event.getStartTime());
        String eventCategory = event.getCategory();
        ParseFile file = event.getImage();

        eventViewHolder.vTitle.setText(eventDetails);
        eventViewHolder.vStartTime.setText(eventStartTime);
        eventViewHolder.vCategory.setText(eventCategory);
        eventViewHolder.vImageView.setParseFile(file);
        eventViewHolder.vImageView.loadInBackground();

        eventViewHolder.setEvent(event);

        if(fromRefresh)
            AnimateUtils.animate(eventViewHolder);
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.event_card, viewGroup, false);
        return new EventRecyclerViewHolder(itemView);
    }
}
