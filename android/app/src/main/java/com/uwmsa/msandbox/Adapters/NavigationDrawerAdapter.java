package com.uwmsa.msandbox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uwmsa.msandbox.Activities.LoginActivity;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Fragments.PrayerLocationMainFragment;
import com.uwmsa.msandbox.Models.NavigationBarOption;
import com.uwmsa.msandbox.R;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

/**
 * Created by Irfan Sharif on 7/13/2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationDrawerViewHolder> {

    private LayoutInflater inflater;
    List <NavigationBarOption> data = Collections.emptyList();
    Context context;
    private ClickListener clickListener;

    public NavigationDrawerAdapter(Context context, List<NavigationBarOption> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public NavigationDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_option, parent, false);

        NavigationDrawerViewHolder viewHolder = new NavigationDrawerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerViewHolder holder, int position) {
        NavigationBarOption currentOption = data.get(position);
        holder.textView.setText(currentOption.title);
        holder.imageView.setImageResource(currentOption.imageId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void SetClickListener (ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class NavigationDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;
        public NavigationDrawerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.navigation_option_text);
            imageView = (ImageView) itemView.findViewById(R.id.navigation_option_image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
//            context.startActivity(new Intent(context, LoginActivity.class));
            if(clickListener != null)
                clickListener.ItemClicked(getPosition());
        }

    }

    public interface ClickListener {
        public void ItemClicked( int position);
    }

}
