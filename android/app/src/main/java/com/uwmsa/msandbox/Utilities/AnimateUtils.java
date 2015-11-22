package com.uwmsa.msandbox.Utilities;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.uwmsa.msandbox.Adapters.EventAdapter;
import com.uwmsa.msandbox.Adapters.PrayerLocationListAdapter.*;
import com.uwmsa.msandbox.Adapters.PrayerLocationJumuahAdapter.*;

/**
 * Created by Irfan Sharif on 8/4/2015.
 */
public class AnimateUtils {
    public static void animate(PrayerLocationDailyRecyclerViewHolder holder) {
        YoYo.with(Techniques.FadeInUp)
                .duration(800)
                .playOn(holder.itemView);
    }

    public static void animate(PrayerLocationJumuahRecyclerViewHolder holder) {
        YoYo.with(Techniques.FadeInUp)
                .duration(800)
                .playOn(holder.itemView);
    }

    public static void animate(EventAdapter.EventRecyclerViewHolder holder) {
        YoYo.with(Techniques.FadeInUp)
                .duration(800)
                .playOn(holder.itemView);
    }
}
