package com.uwmsa.msandbox.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
        protected ParseImageView vImageView;
        protected TextView vStartTime;

        public EventRecyclerViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.event_title);
            vImageView = (ParseImageView) v.findViewById(R.id.event_thumbnail);
            vStartTime = (TextView) v.findViewById(R.id.event_startTime);
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
        String eventDetails = event.getString(Event.TITLE) ;//+ " " + event.getString(Event.DESCRIPTION);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        String eventStartTime = dateFormat.format(event.getDate(Event.STARTTIME));
        ParseFile file = event.getParseFile(Event.IMAGE);

        eventViewHolder.vTitle.setText(eventDetails);
        eventViewHolder.vStartTime.setText(eventStartTime);
        eventViewHolder.vImageView.setParseFile(file);
        eventViewHolder.vImageView.loadInBackground();
    }

    @Override
    public EventRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.event_card, viewGroup, false);

        return new EventRecyclerViewHolder(itemView);
    }




}
