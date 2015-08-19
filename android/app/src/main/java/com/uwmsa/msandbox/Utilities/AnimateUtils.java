package com.uwmsa.msandbox.Utilities;

import android.animation.ObjectAnimator;
import android.os.Handler;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.uwmsa.msandbox.Adapters.EventAdapter;
import com.uwmsa.msandbox.Adapters.PrayerLocationDailyAdapter.*;
import com.uwmsa.msandbox.Adapters.PrayerLocationJumuahAdapter.*;
import com.uwmsa.msandbox.R;

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
