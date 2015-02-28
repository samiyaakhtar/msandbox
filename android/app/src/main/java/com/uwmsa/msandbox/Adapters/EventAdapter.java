package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;
import java.util.List;

/**
 * Created by samiya on 28/02/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventRecyclerViewHolder>  {

    /**
     * Created by samiya on 28/02/15.
     */
    public static class EventRecyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;

        public EventRecyclerViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.event_title);
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
        String eventDetails = event.getString(Event.TITLE) + " " + event.getString(Event.DESCRIPTION);
        eventViewHolder.vTitle.setText(eventDetails);
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.event_card, viewGroup, false);

        return new EventRecyclerViewHolder(itemView);
    }




}
