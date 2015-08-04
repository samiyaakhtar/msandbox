package com.uwmsa.msandbox.Utilities;

import android.animation.ObjectAnimator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.uwmsa.msandbox.Adapters.PrayerLocationDailyAdapter.*;
import com.uwmsa.msandbox.Adapters.PrayerLocationJumuahAdapter.*;

/**
 * Created by Irfan Sharif on 8/4/2015.
 */
public class AnimateUtils {
    public static void animate(PrayerLocationDailyRecyclerViewHolder holder) {
        YoYo.with(Techniques.FadeInLeft)
                .duration(500)
                .playOn(holder.itemView);
    }

    public static void animate(PrayerLocationJumuahRecyclerViewHolder holder) {
        YoYo.with(Techniques.FadeInLeft)
                .duration(500)
                .playOn(holder.itemView);
    }
}
