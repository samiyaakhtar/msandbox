package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.uwmsa.msandbox.Models.*;
import com.uwmsa.msandbox.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by samiya on 28/02/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventRecyclerViewHolder>  {

    private OnEventClickListener mOnClickListener;

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
            v.setOnClickListener(this);
        }
    }

    private List<Event> eventList;

    public EventAdapter(List<Event> listItems) {
        this.eventList = listItems;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewHolder eventViewHolder, int i) {
        Event event = eventList.get(i);
        String eventDetails = event.getTitle() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        String eventStartTime = dateFormat.format(event.getStartTime());
        String eventCategory = event.getCategory();
        ParseFile file = event.getImage();

        eventViewHolder.vTitle.setText(eventDetails);
        eventViewHolder.vStartTime.setText(eventStartTime);
        eventViewHolder.vCategory.setText(eventCategory);
        eventViewHolder.vImageView.setParseFile(file);
        eventViewHolder.vImageView.loadInBackground();

        eventViewHolder.setEvent(event);
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.event_card, viewGroup, false);
        return new EventRecyclerViewHolder(itemView);
    }
}
