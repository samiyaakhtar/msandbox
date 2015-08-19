package com.uwmsa.msandbox.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwmsa.msandbox.Models.NavigationBarOption;
import com.uwmsa.msandbox.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Irfan Sharif on 7/13/2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    List <NavigationBarOption> data = Collections.emptyList();
    Context context;
    private ClickListener clickListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public NavigationDrawerAdapter(Context context, List<NavigationBarOption> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.navigation_header, parent, false);
            NavigationDrawerHeaderHolder viewHolder = new NavigationDrawerHeaderHolder(view);
            return viewHolder;
        } else {
            View view = inflater.inflate(R.layout.navigation_drawer_option, parent, false);
            NavigationDrawerItemHolder viewHolder = new NavigationDrawerItemHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NavigationDrawerHeaderHolder)
        {

        }
        else {
            NavigationDrawerItemHolder itemHolder = (NavigationDrawerItemHolder) holder;
            NavigationBarOption currentOption = data.get(position - 1);
            itemHolder.textView.setText(currentOption.title);
            itemHolder.imageView.setImageResource(currentOption.imageId);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void SetClickListener (ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class NavigationDrawerItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;
        public NavigationDrawerItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.navigation_option_text);
            imageView = (ImageView) itemView.findViewById(R.id.navigation_option_image);
            textView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null)
                clickListener.ItemClicked(getPosition() - 1);
        }
    }

    class NavigationDrawerHeaderHolder extends RecyclerView.ViewHolder {

        public NavigationDrawerHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ClickListener {
        public void ItemClicked( int position);
    }
}
