package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.EventDetailsActivity;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.EventAdapter;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;

import java.util.List;

public class EventListFragment extends Fragment implements EventAdapter.OnEventClickListener {


    /**
     * Used to display events in a list of cards
     */
    private RecyclerView mEventRecyclerView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static EventListFragment newInstance(int sectionNumber) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public void fillEventsRecyclerView() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.CLASSNAME);

        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                EventAdapter eventAdapter = new EventAdapter(events);
                eventAdapter.setOnEventClickListener(EventListFragment.this);
                mEventRecyclerView.setAdapter(eventAdapter);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_list,
                container, false);

        // Set up the event recycler view
        try {
            mEventRecyclerView = (RecyclerView) view.findViewById(R.id.main_eventRecyclerView);
            mEventRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mEventRecyclerView.setLayoutManager(llm);

            fillEventsRecyclerView();
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

    }


    @Override
    public void OnEventClickListener(Event event) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        eventDetailsIntent.putExtras(bundle);

        getActivity().startActivity(eventDetailsIntent);
    }
}
