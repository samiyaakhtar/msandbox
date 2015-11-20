package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

public class EventListFragment extends Fragment implements EventAdapter.OnEventClickListener, SwipeRefreshLayout.OnRefreshListener {


    /**
     * Used to display events in a list of cards
     */
    private RecyclerView mEventRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    boolean constructorCalled;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static EventListFragment newInstance(int sectionNumber) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public EventListFragment() {
        this.constructorCalled = true;
    }

    public void fillEventsRecyclerView() {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.CLASSNAME);
        eventQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    try {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    } catch (NullPointerException ne) {
                        // User has switched fragments to something other than the event list while
                        // there was no network connection, so the toast has no context and throws an error
                        Log.e("Error", ne.getMessage());
                    }
                }
                EventAdapter eventAdapter = new EventAdapter(events,  mSwipeRefreshLayout.isRefreshing() || constructorCalled);
                eventAdapter.setOnEventClickListener(EventListFragment.this);
                mEventRecyclerView.setAdapter(eventAdapter);
                if(mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_list,
                container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshEvent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // Set up the event recycler view
        try {
            mEventRecyclerView = (RecyclerView) view.findViewById(R.id.main_eventRecyclerView);
            mEventRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if(position == 0)
                        return 2;

                    if(position % 3 == 0 ){
                        return 2;
                    }else{
                        return 1;
                    }


                    //Check out the width pattern this creates, at the bottom of this file
                }
            });
            mEventRecyclerView.setLayoutManager(manager);

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
        eventDetailsIntent.putExtra("eventObjectId", event.getObjectId());
        getActivity().startActivity(eventDetailsIntent);
    }

    @Override
    public void onRefresh() {
        fillEventsRecyclerView();
    }
}


/*

Width Pattern
Position ==> Width
0 ==> 2
1 ==> 1
2 ==> 1
3 ==> 2
4 ==> 1
5 ==> 1
6 ==> 2
7 ==> 1
8 ==> 1
9 ==> 2
10 => 1
11 => 1
12 => 2
13 => 1
14 => 1
15 => 2
16 => 1

 */